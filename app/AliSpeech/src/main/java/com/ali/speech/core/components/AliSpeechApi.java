package com.ali.speech.core.components;

import com.ali.speech.core.env.AliProperties;
import com.ali.speech.core.model.Page;
import com.ali.speech.core.model.Vocab;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nls.client.AccessToken;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 封装的阿里sdk对象
 */
@Data
@Slf4j
@Component
public class AliSpeechApi {

    /**
     * 地域信息
     * 常量内容，请勿改变
     */
    private static final String REGION_ID = "cn-shanghai";
    private static final String DOMAIN = "nls-slp.cn-shanghai.aliyuncs.com";
    private static final ProtocolType PROTOCOL_TYPE = ProtocolType.HTTPS;
    /**
     * POP API信息
     * 常量内容，请勿改变
     */
    private static final String API_VERSION = "2018-11-20";
    private static final String ACTION_CREATE_ASR = "CreateAsrVocab";
    private static final String ACTION_GET_ASR_VOCAB = "GetAsrVocab";
    private static final String ACTION_LIST_ASR_VOCAB = "ListAsrVocab";
    private static final String ACTION_UPDATE_ASR_VOCAB = "UpdateAsrVocab";
    private static final String ACTION_DELETE_ASR_VOCAB = "DeleteAsrVocab";
    /**
     * 参数设置key
     * 常量内容，请勿改变
     */
    private static final String KEY_VOCAB_ID = "VocabId";
    private static final String KEY_ID = "Id";
    private static final String KEY_NAME = "Name";
    private static final String KEY_DESCRIPTION = "Description";
    private static final String KEY_WORD_WEIGHTS = "WordWeights";
    private static final String KEY_VOCAB = "Vocab";
    private static final String KEY_PAGE = "Page";
    private static final String KEY_PAGE_NUMBER = "PageNumber";
    private static final String KEY_PAGE_SIZE = "PageSize";

    // 阿里云鉴权client
    private static IAcsClient client;

    @Autowired
    private AliProperties props;

    private IAcsClient getClient() {
        if (null != client) return client;
        DefaultProfile profile = DefaultProfile.getProfile(REGION_ID, props.getAccessKeyId(), props.getAccessKeySecret());
        client = new DefaultAcsClient(profile);
        return client;
    }

    /**
     * 调用阿里sdk获取token
     *
     * @return
     */
    public String createToken() {
        String token = null;
        try {
            AccessToken accessToken = AccessToken.apply(props.getAccessKeyId(), props.getAccessKeySecret());
            token = accessToken.getToken();
            log.info("Created token: " + token +
                    // 有效时间，单位为秒
                    ", expire time(s): " + accessToken.getExpireTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    private CommonRequest newRequest(String action) {
        CommonRequest request = new CommonRequest();
        request.setDomain(DOMAIN);
        request.setProtocol(PROTOCOL_TYPE);
        request.setVersion(API_VERSION);
        request.setMethod(MethodType.POST);
        request.setAction(action);
        return request;
    }

    // 以下是业务代码
    /**
     * 创建词表
     *
     * @param name 词表名称，必填
     * @param description 词表描述信息，可选
     * @param wordWeights 词表里的词和对应的权重，JSON的Map格式，必填
     *
     * @return String 创建的词表Id
     */
    public String createAsrVocab(String name, String description, String wordWeights) {
        CommonRequest request = newRequest(ACTION_CREATE_ASR);
        request.putBodyParameter(KEY_NAME, name);
        request.putBodyParameter(KEY_DESCRIPTION, description);
        request.putBodyParameter(KEY_WORD_WEIGHTS, wordWeights);
        CommonResponse response = null;
        try {
            response = getClient().getCommonResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        if (response.getHttpStatus() != 200) {
            log.info(response.getData());
            log.info("创建词表失败，HTTP错误码：" + response.getHttpStatus());
            return null;
        }
        JSONObject result = JSONObject.parseObject(response.getData());
        String vocabId = result.getString(KEY_VOCAB_ID);
        return vocabId;
    }
    /**
     * 获取词表
     *
     * @param vocabId 词表Id
     *
     * @return Vocab 获取的词表对象
     */
    public Vocab getAsrVocab(String vocabId) {
        CommonRequest request = newRequest(ACTION_GET_ASR_VOCAB);
        request.putBodyParameter(KEY_ID, vocabId);
        CommonResponse response = null;
        try {
            response = getClient().getCommonResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        if (response.getHttpStatus() != 200) {
            log.info(response.getData());
            log.info("获取词表失败，HTTP错误码：" + response.getHttpStatus());
            return null;
        }
        JSONObject result = JSONObject.parseObject(response.getData());
        String vocabJson = result.getString(KEY_VOCAB);
        Vocab vocab = JSONObject.parseObject(vocabJson, Vocab.class);
        return vocab;
    }
    /**
     * 更新词表
     *
     * @param vocabId 待更新的词表Id
     * @param name 更新后的词表名称
     * @param description 更新后的词表描述
     * @param wordWeights 更新后的热词和权重
     *
     * @return boolean 更新词表是否成功
     */
    public boolean updateAsrVocab(String vocabId, String name, String description, String wordWeights) {
        CommonRequest request = newRequest(ACTION_UPDATE_ASR_VOCAB);
        request.putBodyParameter(KEY_ID, vocabId);
        request.putBodyParameter(KEY_NAME, name);
        request.putBodyParameter(KEY_DESCRIPTION, description);
        request.putBodyParameter(KEY_WORD_WEIGHTS, wordWeights);
        CommonResponse response = null;
        try {
            response = getClient().getCommonResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        if (response.getHttpStatus() != 200) {
            log.info(response.getData());
            log.info("更新词表失败，HTTP错误码：" + response.getHttpStatus());
            return false;
        }
        return true;
    }
    /**
     * 删除词表
     *
     * @param vocabId 词表Id
     *
     * @return boolean 删除词表是否成功
     * */
    public boolean deleteAsrVocab(String vocabId) {
        CommonRequest request = newRequest(ACTION_DELETE_ASR_VOCAB);
        request.putBodyParameter(KEY_ID, vocabId);
        CommonResponse response = null;
        try {
            response = getClient().getCommonResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        if (response.getHttpStatus() != 200) {
            log.info(response.getData());
            log.info("删除词表失败, HTTP错误码：" + response.getHttpStatus());
            return false;
        }
        return true;
    }
    /**
     * 列举词表
     * 如果不指定获取的页号，默认获取第1页
     * 如果不指定每页的词表数量，默认每页10个词表
     *
     * @return Page 所有词表信息
     */
    public Page listAsrVocab() {
        CommonRequest request = newRequest(ACTION_LIST_ASR_VOCAB);
        request.putBodyParameter(KEY_PAGE_NUMBER, 1);
        request.putBodyParameter(KEY_PAGE_SIZE, 10);
        CommonResponse response = null;
        try {
            response = getClient().getCommonResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        if (response.getHttpStatus() != 200) {
            log.info(response.getData());
            log.info("列举词表失败，HTTP错误码：" + response.getHttpStatus());
            return null;
        }
        JSONObject result = JSONObject.parseObject(response.getData());
        String pageJson = result.getString(KEY_PAGE);
        Page page = JSONObject.parseObject(pageJson, Page.class);
        return page;
    }
}
