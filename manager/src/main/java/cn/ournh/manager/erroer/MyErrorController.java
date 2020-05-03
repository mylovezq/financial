package cn.ournh.manager.erroer;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class MyErrorController extends BasicErrorController {
    public MyErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorProperties, errorViewResolvers);
    }

//    {
//        "timestamp": "2020-04-27 23:55:46.916",
//            "status": 500,
//            "error": "Internal Server Error",
//            "message": "编号不能为空！",
//            "path": "/manager/products/
    //+code
    //canRetry
//    }
//    @Override
//    protected Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
//        Map<String, Object> attrs = super.getErrorAttributes(request, includeStackTrace);
//        attrs.remove("timestamp");
//        attrs.remove("status");
//        attrs.remove("error");
//        attrs.remove("path");
//        String errorCode = (String) attrs.get("message");
//        ErrorEnum errorEnum = ErrorEnum.getByCode(errorCode);
//        attrs.put("message",errorEnum.getMessage());
//        attrs.put("code",errorEnum.getCode());
//        attrs.put("canRetry",errorEnum.isCanRetry());
//        return attrs;
//    }
}
