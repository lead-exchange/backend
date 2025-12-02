package lead.exchange.security;

import jakarta.servlet.http.HttpServletRequest;
import lead.exchange.exception.ForbiddenException;
import lead.exchange.security.models.CurrentUser;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return CurrentUser.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            @NotNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            org.springframework.web.bind.support.WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        CurrentUser cu = (CurrentUser) request.getAttribute("currentUser");
        if (cu == null) {
            throw new ForbiddenException("Not authenticated");
        }
        return cu;
    }
}
