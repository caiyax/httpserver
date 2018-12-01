package com.bupt;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpHandle extends ChannelInboundHandlerAdapter {
    private static Map<String,ActionEntity> map;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        map=ActionMap.getMap();
        if (msg instanceof DefaultHttpRequest){
            DefaultHttpRequest request= (DefaultHttpRequest) msg;
            String uri=request.uri();
            QueryStringDecoder  queryStringDecoder=
                    new QueryStringDecoder(URLDecoder.decode(uri,"utf-8"));
            String url=queryStringDecoder.path();
            Map<String,String> parMap=buildParamMap(queryStringDecoder);
            ActionEntity actionEntity=map.get(url);

            ResponseEntity entity=execute(actionEntity,parMap);

            responseMsg(ctx, JSON.toJSONString(entity));
        }
    }
    private void responseMsg(ChannelHandlerContext ctx, String res){
        DefaultFullHttpResponse response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, Unpooled.copiedBuffer(res, CharsetUtil.UTF_8));
        buildHead(response);
        ctx.writeAndFlush(response);
    }

    /*执行需要调用的方法*/
    private ResponseEntity execute(ActionEntity actionEntity,Map<String,String> parMap) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Class cls=actionEntity.getCls();
        AbsAction action= (AbsAction) cls.newInstance();
        Method[] methods = cls.getDeclaredMethods();
        ResponseEntity entity=null;
        for (Method m:methods){
            if (m.getName().equals(actionEntity.getMethod())){
                Parameter[] paras = m.getParameters();
                Object[] objects=new Object[paras.length];
                int index=0;
                for (Parameter p:paras){
                    String value = p.getAnnotation(Param.class).value();
                    Class type=p.getType();
                    Object typeValue=parMap.get(value);
                    if (type.equals(int.class)||type.equals(Integer.class)){
                        typeValue = Integer.valueOf(typeValue.toString());
                    }
                    objects[index++] = typeValue;
                }
                entity= (ResponseEntity) m.invoke(action,objects);
            }
        }
        return entity;
    }

    /*根据前端的url构造参数列表*/
    private Map<String,String> buildParamMap(QueryStringDecoder  queryStringDecoder){
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        Map<String,String> parMap=new HashMap<>();
        parameters.forEach((k,v)->{
            parMap.put(k,v.get(0));
        });
        return parMap;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ResponseEntity workRes = new ResponseEntity() ;
        workRes.setCode(String.valueOf(HttpResponseStatus.NOT_FOUND.code()));
        workRes.setMessage(cause.getMessage());

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, Unpooled.copiedBuffer(JSON.toJSONString(workRes), CharsetUtil.UTF_8)) ;
        buildHead(response);
        ctx.writeAndFlush(response);
    }


    private void buildHead(DefaultFullHttpResponse response){
        HttpHeaders headers=response.headers();
        headers.setInt(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes());
        headers.set(HttpHeaderNames.CONTENT_TYPE,"application/json");
    }
}
