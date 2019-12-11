package la.niub.abcapi.gateway.component.filter.error;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import la.niub.abcapi.gateway.config.Constants;
import la.niub.abcapi.gateway.config.code.BaseCodeEnum;
import la.niub.abcapi.gateway.model.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

@Component
public class ErrorFilter extends ZuulFilter {

//    private final static Logger logger = LogManager.getLogger(ErrorFilter.class);

    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }


    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run(){
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setSendZuulResponse(false);
        ctx.setResponseBody(JSON.toJSONString(new Response(BaseCodeEnum.ERROR_SERVICE.getCode(), BaseCodeEnum.ERROR_SERVICE.getMessage())));
        ctx.set(Constants.GO_TO_NEXT_STEP, false);
        ctx.set("sendErrorFilter.ran",false);
        return null;
    }

}
