package kma.ktlt.post.app.exception;


import kma.ktlt.post.domain.common.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    Handle exception validation api validation request dto
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseMultipleField handleValidationException(Exception exception , WebRequest webRequest , MethodArgumentNotValidException methodArgumentNotValidException){
        methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
        Map<String, String> mess = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            mess.put(fieldName, errorMessage);
        });
        ErrorResponseMultipleField errorResponse = new ErrorResponseMultipleField();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(webRequest.getDescription(false).replace("uri=" , ""));
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage(mess);
        errorResponse.setType(ErrorType.MULTIPLE);
        return errorResponse;

    }
    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAppException(AppException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(e.getErrorCode().getStatusCode().value());
        errorResponse.setError(AppException.class.getSimpleName());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setCode(e.getErrorCode().getCode());
        errorResponse.setType(ErrorType.SINGLE);
        return errorResponse;
    }
//
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setType(ErrorType.SINGLE);
        errorResponse.setMessage("Dữ liệu yêu cầu không hợp lệ. Vui lòng kiểm tra lại định dạng dữ liệu và thử lại.");
        return errorResponse;
    }
////
////
    @ExceptionHandler({ NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoResourceFoundException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage("Not found 404");
        errorResponse.setType(ErrorType.SINGLE);

        return errorResponse;
    }

    @ExceptionHandler({ AuthorizationDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handAuthorizationDeniedException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        errorResponse.setMessage("403");
        errorResponse.setType(ErrorType.SINGLE);

        return errorResponse;
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage("Param invalid");
        errorResponse.setType(ErrorType.SINGLE);

        return errorResponse;
    }
//
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGlobalException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorResponse.setMessage("Đã xảy ra lỗi hệ thống");
        errorResponse.setType(ErrorType.SINGLE);

        return errorResponse;
    }
//
////param invalid
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseMultipleField handleConstraintViolationException(ConstraintViolationException exception, WebRequest webRequest) {
        Map<String, String> messages = exception.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> extractFieldName(violation.getPropertyPath().toString()),
                        violation -> violation.getMessage()
                ));

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Param invalid",
                webRequest.getDescription(false).replace("uri=", ""),
                messages
        );
    }



    //helper function
    private String extractFieldName(String propertyPath) {
        String[] parts = propertyPath.split("\\.");
        return parts.length == 2 ? parts[1] : propertyPath;
    }

    private ErrorResponseMultipleField buildErrorResponse(HttpStatus status, String error, String path, Map<String, String> messages) {
        ErrorResponseMultipleField response = new ErrorResponseMultipleField();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(status.value());
        response.setPath(path);
        response.setError(error);
        response.setMessage(messages);
        response.setType(ErrorType.MULTIPLE);
        return response;
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException ex ,  WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(webRequest.getDescription(false).replace("uri=", ""));
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage("Thiếu tham số bắt buộc: " + ex.getParameterName());
        return errorResponse;
    }



//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setTimestamp(new Date());
//        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
//        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        errorResponse.setError(e.getClass().getSimpleName());
//        errorResponse.setMessage(e.getMessage());
//        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }


//    could not execute statement [Duplicate entry 'manhk18kma@gmai1l.com' for key 'tbl_user.email_UNIQUE'] [insert into tbl_user (create_by,created_at,date_of_birth,email,full_name,type,password,phone,status,token_device,updated_at,updated_by,username) values (?,?,?,?,?,?,?,?,?,?,?,?,?)]; SQL [insert into tbl_user (create_by,created_at,date_of_birth,email,full_name,type,password,phone,status,token_device,updated_at,updated_by,username) values (?,?,?,?,?,?,?,?,?,?,?,?,?)]; constraint [tbl_user.email_UNIQUE]
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setTimestamp(new Date());
//        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
//        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//        errorResponse.setError("Invalid Data");
//
//        String message = e.getMessage();
//        String constraintInfo = extractConstraintInfo(message);
//        String tableName = extractTableName(constraintInfo);
//        String constraintName = extractConstraintName(constraintInfo);
//        String columnName = extractColumnName(constraintName);
//
//        if ("tbl_user".equals(tableName)) {
//            if ("email_UNIQUE".equals(constraintName)) {
//                errorResponse.setMessage("Email is already used.");
//            } else if ("username_UNIQUE".equals(constraintName)) {
//                errorResponse.setMessage("Username is already used.");
//            } else if ("phone_UNIQUE".equals(constraintName)) {
//                errorResponse.setMessage("Phone number is already used.");
//            }
//        } else {
//            errorResponse.setMessage("Data integrity violation.");
//        }
//        return errorResponse;
//    }
//



    private String extractConstraintInfo(String message) {
        int firstIndex = message.lastIndexOf("constraint [");
        int lastIndex = message.lastIndexOf("]");

        if (firstIndex == -1 || lastIndex == -1 || firstIndex >= lastIndex) {
            return "";
        }

        return message.substring(firstIndex + "constraint [".length(), lastIndex);
    }

    private String extractTableName(String constraintInfo) {
        String[] parts = constraintInfo.split("\\.");
        return parts.length == 2 ? parts[0] : "?";
    }

    private String extractColumnName(String constraintName) {
        String[] constraintParts = constraintName.split("_");
        return constraintParts.length > 1 ? constraintParts[0] : "?";
    }

    private String extractConstraintName(String constraintInfo) {
        String[] parts = constraintInfo.split("\\.");
        return parts.length == 2 ? parts[1] : "?";
    }


}




//
//   if ("tbl_user".equals(tableName)) {
//           if ("email_UNIQUE".equals(constraintName)) {
//           errorResponse.setMessage("Email is already used.");
//           } else if ("username_UNIQUE".equals(constraintName)) {
//           errorResponse.setMessage("Username is already used.");
//           } else if ("phone_UNIQUE".equals(constraintName)) {
//           errorResponse.setMessage("Phone number is already used.");
//           }
//           } else {
//           errorResponse.setMessage("Data integrity violation.");
//           }
