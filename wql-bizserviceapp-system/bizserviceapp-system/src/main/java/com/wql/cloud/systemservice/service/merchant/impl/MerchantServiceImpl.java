package com.wql.cloud.systemservice.service.merchant.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wql.cloud.basic.redis.util.RedisUtil;
import com.wql.cloud.systemservice.mapper.api.ApiMapper;
import com.wql.cloud.systemservice.mapper.api.ApiPermissionMapper;
import com.wql.cloud.systemservice.mapper.merchant.MerchantMapper;
import com.wql.cloud.systemservice.mapper.merchant.MerchantProductMapper;
import com.wql.cloud.systemservice.mapper.merchant.MerchantWhitelistMapper;
import com.wql.cloud.systemservice.pojo.domain.api.Api;
import com.wql.cloud.systemservice.pojo.domain.api.ApiPermission;
import com.wql.cloud.systemservice.pojo.domain.merchant.Merchant;
import com.wql.cloud.systemservice.pojo.domain.merchant.MerchantProduct;
import com.wql.cloud.systemservice.pojo.domain.merchant.MerchantWhitelist;
import com.wql.cloud.systemservice.pojo.res.MerchantCacheInfo;
import com.wql.cloud.systemservice.service.merchant.MerchantService;
import com.wql.cloud.tool.collect.CollectionUtils;
import com.wql.cloud.tool.json.JsonUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;

@Service
public class MerchantServiceImpl implements MerchantService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	public static final String SYSTEM_MERCHANT = "system:merchant:";
	 
	@Autowired
	private MerchantMapper merchantMapper;
	@Autowired
	private MerchantProductMapper merchantProductMapper;
	@Autowired
	private MerchantWhitelistMapper merchantWhitelistMapper;
	@Autowired
	private ApiMapper apiMapper;
	@Autowired
	private ApiPermissionMapper apiPermissionMapper;
	@Autowired
	private RedisUtil redisUtil;
	
	@XxlJob("merchantJobHandler")
    public ReturnT<String> loadMerchantCache(String param) throws Exception {
		//查询商户信息
		Merchant record1 = new Merchant();
		record1.setDataFlag(1);
		record1.setState(1);
		List<Merchant> merchantList = merchantMapper.select(record1);
		if(CollectionUtils.isEmpty(merchantList)) {
			redisUtil.remove(SYSTEM_MERCHANT);
			return ReturnT.SUCCESS;
		}
		//查询白名单
		MerchantWhitelist record2 = new MerchantWhitelist();
		record2.setDataFlag(1);
		record2.setWhiteState(1);
		List<MerchantWhitelist> whitelists = merchantWhitelistMapper.select(record2);
		//查询商户产品权限
		MerchantProduct record3 = new MerchantProduct();
		record3.setDataFlag(1);
		record3.setState(1);
		List<MerchantProduct> mertProducts = merchantProductMapper.select(record3);
		//查询api列表
		Api record4 = new Api();
		record4.setDataFlag(1);
		record4.setApiState(1);
		List<Api> apiList = apiMapper.select(record4);
		//查询产品权限
		ApiPermission record5 = new ApiPermission();
		record5.setDataFlag(1);
		record5.setApiPermissionState(1);
		List<ApiPermission> apiPermissionList = apiPermissionMapper.select(record5);
		//组合数据
		for (Merchant merchant : merchantList) {
			MerchantCacheInfo info = new MerchantCacheInfo();
			//商户密钥
			info.setMerchantCode(merchant.getMerchantCode());
			info.setMerchantPrivateKey(merchant.getMerchantPrivateKey());
			info.setMerchantPublicKey(merchant.getMerchantPublicKey());
			info.setPlatformPrivateKey(merchant.getPlatformPrivateKey());
			info.setPlatformPublicKey(merchant.getPlatformPublicKey());
			//商户白名单
			List<String> whiteList = whitelists.stream()
					.filter(o -> o.getMerchantCode().equals(merchant.getMerchantCode()))
					.map(MerchantWhitelist::getWhiteIp)
					.collect(Collectors.toList());
			info.setWhiteList(whiteList);
			//开始组装-商户apikey
			List<String> productCodes = mertProducts.stream()
				.filter(o -> o.getMerchantCode().equals(merchant.getMerchantCode()))
				.map(MerchantProduct::getProductCode)
				.collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(productCodes)) {
				//过滤该商户开通的产品code集合
				List<String> productCodeList = apiPermissionList.stream()
						.map(ApiPermission::getProductCode)
						.distinct()
						.collect(Collectors.toList());
				//过滤该商户开通了哪些api组集合
				List<String> apiGroupCodes = apiPermissionList.stream()
						.filter(o -> productCodeList.contains(o.getProductCode()))
						.map(ApiPermission::getApiGroupCode).distinct()
						.collect(Collectors.toList());
				//过滤该产品code集合，有哪些apikey
				List<String> apikeyList = apiList.stream()
						.filter(o -> apiGroupCodes.contains(o.getApiGroupCode()))
						.map(Api::getApiKey)
						.collect(Collectors.toList());
				info.setApiList(apikeyList);	
			}
			redisUtil.set(SYSTEM_MERCHANT.concat(merchant.getMerchantCode()),  JsonUtils.toJsonString(info));
		}
        return ReturnT.SUCCESS;
	}
	
}
