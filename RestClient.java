package com.pg.mpt.ppt.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.pg.mpt.ppt.support.JsonUtil.serialize;

/**
 * @author
 */
@Configuration
public class RestClient{
    @Autowired
    private ObjectMapper objectMapper;


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        builder.setConnectTimeout(6000);
        builder.setReadTimeout(6000);

        RestTemplate restTemplate = builder.build();


        List<HttpMessageConverter<?>> messageConverters = Lists.newArrayList();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        //不加可能会出现异常
        //Could not extract response: no suitable HttpMessageConverter found for response type [class ]

        MediaType[] mediaTypes = new MediaType[]{
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_OCTET_STREAM,

            MediaType.TEXT_HTML,
            MediaType.TEXT_PLAIN,
            MediaType.TEXT_XML,
            MediaType.APPLICATION_ATOM_XML,
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON_UTF8,
            MediaType.APPLICATION_PDF,
        };

        converter.setSupportedMediaTypes(Arrays.asList(mediaTypes));

        try {
            //通过反射设置MessageConverters
            Field field = restTemplate.getClass().getDeclaredField("messageConverters");

            field.setAccessible(true);

            List<HttpMessageConverter<?>> orgConverterList = (List<HttpMessageConverter<?>>) field.get(restTemplate);

            Optional<HttpMessageConverter<?>> opConverter = orgConverterList.stream()
                .filter(x -> x.getClass().getName().equalsIgnoreCase(MappingJackson2HttpMessageConverter.class
                    .getName()))
                .findFirst();

            if (opConverter.isPresent() == false) {
                return restTemplate;
            }

            messageConverters.add(converter);//添加MappingJackson2HttpMessageConverter

            //添加原有的剩余的HttpMessageConverter
            List<HttpMessageConverter<?>> leftConverters = orgConverterList.stream()
                .filter(x -> x.getClass().getName().equalsIgnoreCase(MappingJackson2HttpMessageConverter.class
                    .getName()) == false)
                .collect(Collectors.toList());

            messageConverters.addAll(leftConverters);

            System.out.println(String.format("【HttpMessageConverter】原有数量：%s，重新构造后数量：%s"
                , orgConverterList.size(), messageConverters.size()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

    /**
     *
     * @param host
     * @return
     */

    public static String get(String host,Map<String,String>map,RestTemplate restTemplate) {
        String url=host;
        StringBuilder stringBuilder=new StringBuilder();
        if(map!=null&&map.size()>0){
            map.forEach((key,value)->{
                if(StringUtils.isNotBlank(key)){
                    if (stringBuilder.length()>0)
                        stringBuilder.append("&");
                    stringBuilder.append(key);
                    stringBuilder.append("=");
                    stringBuilder.append(value);
                }
            });
        }
        if(stringBuilder.length()>0){
             url=host+"?"+stringBuilder.toString();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<Object> response=restTemplate.exchange(url, HttpMethod.GET,entity,Object.class);


        return String.valueOf(response.getBody()) ;
    }
    /**
     * json格式的post提交
     * @param obj
     * @param url
     * @return
     */
    public static String postJson(String url, Object obj,RestTemplate restTemplate) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        String result = null;
        if(obj == null){
            result = "{}";
        }else{
            result = serialize(obj);
        }
        HttpEntity<String> formEntity = new HttpEntity<String>(result,headers);
        return restTemplate.postForObject(url , formEntity, String.class);
    }

    /**
     * form格式的post提交
     * @param url
     * @param headerMap
     * @param params
     * @return
     */
    public static String postForm(String url,Map<String , String> headerMap, MultiValueMap<String, String> params,RestTemplate restTemplate){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        for(Map.Entry<String ,String> entry : headerMap.entrySet()){
            headers.add(entry.getKey() , entry.getValue());
        }
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class).getBody();
    }
}
