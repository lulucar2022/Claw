package com.claw.common.exception;

import com.claw.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常: {} - {}", e.getCode(), e.getMessage(), e);
        return ApiResponse.error(e.getCode(), e.getMessage())
                .withRequestId(getRequestId(request));
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        
        log.warn("参数验证失败: {}", message);
        
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "参数错误"
                ));
        
        return ApiResponse.<Map<String, String>>badRequest("参数验证失败")
                .withRequestId(getRequestId(request))
                .setData(errors);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleBindException(BindException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        
        log.warn("参数绑定失败: {}", message);
        return ApiResponse.badRequest(message)
                .withRequestId(getRequestId(request));
    }

    /**
     * 处理参数约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        
        log.warn("参数约束违反: {}", message);
        return ApiResponse.badRequest(message)
                .withRequestId(getRequestId(request));
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        String message = "缺少必要参数: " + e.getParameterName();
        log.warn(message);
        return ApiResponse.badRequest(message)
                .withRequestId(getRequestId(request));
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String message = "参数类型不匹配: " + e.getName() + " 应为 " + e.getRequiredType().getSimpleName();
        log.warn(message);
        return ApiResponse.badRequest(message)
                .withRequestId(getRequestId(request));
    }

    /**
     * 处理HTTP消息不可读异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("HTTP消息不可读: {}", e.getMessage());
        return ApiResponse.badRequest("请求体格式错误或无法解析")
                .withRequestId(getRequestId(request));
    }

    /**
     * 处理HTTP方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResponse<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("HTTP方法不支持: {}", e.getMethod());
        return ApiResponse.error(405, "不支持的HTTP方法: " + e.getMethod())
                .withRequestId(getRequestId(request));
    }

    /**
     * 处理资源不存在异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.warn("资源不存在: {} {}", e.getHttpMethod(), e.getRequestURL());
        return ApiResponse.notFound("请求的资源不存在")
                .withRequestId(getRequestId(request));
    }

    /**
     * 处理文件大小超出限制异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        log.warn("文件大小超出限制: {}", e.getMessage());
        return ApiResponse.badRequest("文件大小超出限制")
                .withRequestId(getRequestId(request));
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleBadCredentialsException(BadCredentialsException e, HttpServletRequest request) {
        log.warn("认证失败: {}", e.getMessage());
        return ApiResponse.unauthorized("用户名或密码错误")
                .withRequestId(getRequestId(request));
    }

    /**
     * 处理权限不足异常
     */
    @ExceptionHandler({org.springframework.security.access.AccessDeniedException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> handleAccessDeniedException(Exception e, HttpServletRequest request) {
        log.warn("权限不足: {}", e.getMessage());
        return ApiResponse.forbidden("权限不足，无法访问该资源")
                .withRequestId(getRequestId(request));
    }

    /**
     * 处理认证不足异常
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleInsufficientAuthenticationException(InsufficientAuthenticationException e, HttpServletRequest request) {
        log.warn("认证不足: {}", e.getMessage());
        return ApiResponse.unauthorized("需要登录认证")
                .withRequestId(getRequestId(request));
    }

    /**
     * 处理数据不存在异常
     */
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleDataNotFoundException(DataNotFoundException e, HttpServletRequest request) {
        log.warn("数据不存在: {}", e.getMessage());
        return ApiResponse.notFound(e.getMessage())
                .withRequestId(getRequestId(request));
    }

    /**
     * 处理数据已存在异常
     */
    @ExceptionHandler(DataAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<?> handleDataAlreadyExistsException(DataAlreadyExistsException e, HttpServletRequest request) {
        log.warn("数据已存在: {}", e.getMessage());
        return ApiResponse.error(409, e.getMessage())
                .withRequestId(getRequestId(request));
    }

    /**
     * 处理数据验证异常
     */
    @ExceptionHandler(DataValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleDataValidationException(DataValidationException e, HttpServletRequest request) {
        log.warn("数据验证失败: {}", e.getMessage());
        return ApiResponse.badRequest(e.getMessage())
                .withRequestId(getRequestId(request));
    }

    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: {}", e.getMessage(), e);
        
        // 生产环境下隐藏详细错误信息
        String message = "系统内部错误，请稍后重试";
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("requestId", getRequestId(request));
        
        return ApiResponse.<Map<String, Object>>internalServerError(message)
                .withRequestId(getRequestId(request))
                .setData(errorDetails);
    }

    /**
     * 获取请求ID
     */
    private String getRequestId(HttpServletRequest request) {
        // 从请求头或属性中获取请求ID
        String requestId = request.getHeader("X-Request-ID");
        if (requestId == null || requestId.isEmpty()) {
            requestId = (String) request.getAttribute("X-Request-ID");
        }
        return requestId;
    }
}