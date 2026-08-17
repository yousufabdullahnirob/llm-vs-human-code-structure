package com.example.metadata.jdbi3;

import java.util.List;
import java.util.UUID;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

/**
 * Top-level data-access interface. In real usage, JDBI generates an
 * implementation of this (and all its nested sub-interfaces) at runtime, so
 * you never write the SQL execution code by hand — just declare the query
 * and the return type.
 *
 * This shows the shape of the pattern used across a larger system: one
 * top-level DAO composed of per-entity sub-DAOs, each following the same
 * conventions (cursor-based pagination, MySQL/Postgres dialect switching
 * via @Define, batch inserts, and a shared relationship table for joins
 * like "team owns table").
 */
public interface CollectionDAO {

  UserDAO userDAO();

  TeamDAO teamDAO();

  RelationshipDAO relationshipDAO();

  /** DAO for a single entity type: User. */
  @RegisterRowMapper(UserRowMapper.class)
  interface UserDAO extends SqlObject {

    @SqlUpdate("INSERT INTO user_entity (id, name, email, json) VALUES (:id, :name, :email, :json)")
    void insert(@Bind("id") String id, @Bind("name") String name, @Bind("email") String email, @Bind("json") String json);

    @SqlQuery("SELECT json FROM user_entity WHERE id = :id")
    String findJsonById(@Bind("id") String id);

    @SqlQuery("SELECT json FROM user_entity WHERE name = :name")
    String findJsonByName(@Bind("name") String name);

    @SqlUpdate("DELETE FROM user_entity WHERE id = :id")
    void delete(@Bind("id") String id);

    /**
     * Cursor-based pagination: instead of OFFSET (slow on large tables),
     * page by the last-seen sort key. @Define lets the same interface emit
     * MySQL vs. Postgres syntax for the "next page" comparison.
     */
    @SqlQuery(
        "SELECT json FROM user_entity "
            + "WHERE (<afterName> IS NULL OR name > <afterName>) "
            + "ORDER BY name "
            + "LIMIT :limit")
    List<String> listAfter(
        @Define("afterName") String afterName, @Bind("afterName") String afterNameValue, @Bind("limit") int limit);

    @SqlBatch("INSERT INTO user_entity (id, name, email, json) VALUES (:id, :name, :email, :json)")
    void insertBatch(@BindBean List<UserRecord> users);
  }

  /** DAO for a single entity type: Team. Same conventions as UserDAO. */
  @RegisterRowMapper(TeamRowMapper.class)
  interface TeamDAO extends SqlObject {

    @SqlUpdate("INSERT INTO team_entity (id, name, json) VALUES (:id, :name, :json)")
    void insert(@Bind("id") String id, @Bind("name") String name, @Bind("json") String json);

    @SqlQuery("SELECT json FROM team_entity WHERE id = :id")
    String findJsonById(@Bind("id") String id);

    @SqlQuery(
        "SELECT json FROM team_entity "
            + "WHERE (<afterName> IS NULL OR name > <afterName>) "
            + "ORDER BY name "
            + "LIMIT :limit")
    List<String> listAfter(
        @Define("afterName") String afterName, @Bind("afterName") String afterNameValue, @Bind("limit") int limit);
  }

  /**
   * Generic relationship table shared across all entity types, used to
   * model joins like "team owns table" or "user follows dashboard" without
   * a dedicated join table per relationship kind.
   */
  interface RelationshipDAO extends SqlObject {

    @SqlUpdate(
        "INSERT INTO entity_relationship (from_id, from_type, to_id, to_type, relation) "
            + "VALUES (:fromId, :fromType, :toId, :toType, :relation)")
    void insert(
        @Bind("fromId") String fromId,
        @Bind("fromType") String fromType,
        @Bind("toId") String toId,
        @Bind("toType") String toType,
        @Bind("relation") String relation);

    @SqlQuery(
        "SELECT to_id FROM entity_relationship "
            + "WHERE from_id = :fromId AND from_type = :fromType AND relation = :relation")
    List<String> findRelatedIds(
        @Bind("fromId") String fromId, @Bind("fromType") String fromType, @Bind("relation") String relation);

    @SqlUpdate(
        "DELETE FROM entity_relationship WHERE from_id = :fromId AND to_id = :toId AND relation = :relation")
    void delete(@Bind("fromId") String fromId, @Bind("toId") String toId, @Bind("relation") String relation);
  }

  // --- Minimal supporting types just so this example compiles standalone ---

  record UserRecord(String id, String name, String email, String json) {
    static UserRecord newUser(String name, String email, String json) {
      return new UserRecord(UUID.randomUUID().toString(), name, email, json);
    }
  }

  class UserRowMapper implements org.jdbi.v3.core.mapper.RowMapper<String> {
    @Override
    public String map(java.sql.ResultSet rs, org.jdbi.v3.core.statement.StatementContext ctx) throws java.sql.SQLException {
      return rs.getString("json");
    }
  }

  class TeamRowMapper implements org.jdbi.v3.core.mapper.RowMapper<String> {
    @Override
    public String map(java.sql.ResultSet rs, org.jdbi.v3.core.statement.StatementContext ctx) throws java.sql.SQLException {
      return rs.getString("json");
    }
  }
}
