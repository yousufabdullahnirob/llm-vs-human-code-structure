package com.example.webapp.exception;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import org.eclipse.jetty.ee10.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;

/**
 * Custom error page handler that attaches security headers (HSTS, CSP,
 * frame options, etc.) to error responses before Jetty renders them.
 *
 * The original problem this solves: Jetty's default error handler bypasses
 * the normal response filter chain, so security headers added elsewhere in
 * the app don't make it onto error pages unless we add them here explicitly.
 */
public class SecureErrorPageHandler extends ErrorPageErrorHandler {

  private final WebSecurityConfig securityConfig;

  public SecureErrorPageHandler(WebSecurityConfig securityConfig) {
    this.securityConfig = securityConfig;
  }

  @Override
  protected boolean generateAcceptableResponse(
      Request request,
      Response response,
      Callback callback,
      String contentType,
      List<Charset> charsets,
      int code,
      String message,
      Throwable cause)
      throws IOException {
    applySecurityHeaders(securityConfig, response);
    return super.generateAcceptableResponse(
        request, response, callback, contentType, charsets, code, message, cause);
  }

  /** Applies configured security headers to a Jetty (ee10) response. */
  public static void applySecurityHeaders(WebSecurityConfig config, Response response) {
    for (Map.Entry<String, String> header : config.buildHeaders().entrySet()) {
      response.getHeaders().put(new HttpField(header.getKey(), header.getValue()));
    }
  }

  /** Same headers, applied to a plain servlet response (used outside Jetty's error path). */
  public static void applySecurityHeaders(WebSecurityConfig config, HttpServletResponse response) {
    config.buildHeaders().forEach(response::setHeader);
  }

  /**
   * Stand-in for the real configuration object: collects the enabled
   * security headers (HSTS, frame options, CSP, referrer policy, etc.) into
   * a single name-to-value map that both response types above can apply.
   * A real implementation would build CSP with a per-request nonce.
   */
  public interface WebSecurityConfig {
    Map<String, String> buildHeaders();
  }
}
