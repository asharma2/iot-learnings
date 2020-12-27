package com.iot.learnings.reverse.geo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import com.iot.learnings.reverse.geo.model.ReverseGeoSerializableFactory;

@Configuration
public class HazelcastConfig {
	@Autowired
	Environment environment;

	@Bean
	Config config() { // (1)
		Config config = new Config();

		NetworkConfig network = config.getNetworkConfig();
		network.setPort(5700).setPortCount(20);
		network.setPortAutoIncrement(true);
		JoinConfig join = network.getJoin();
		join.getMulticastConfig().setEnabled(false);
		network.getJoin().getTcpIpConfig().setEnabled(true);
		network.setPortAutoIncrement(true);

		String members = environment.getProperty("hazelcast.members", String.class);
		if (!StringUtils.isEmpty(members))
			network.getJoin().getTcpIpConfig().addMember(members);

		ManagementCenterConfig mcc = new ManagementCenterConfig().setUrl("http://127.0.0.1:38080/mancenter")
				.setEnabled(false);
		config.setManagementCenterConfig(mcc);

		config.setInstanceName("ReverseGeo") //
				.addMapConfig(new MapConfig().setName("revgeo") //
						.setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE)) //
						.setEvictionPolicy(EvictionPolicy.LRU) //
						.setTimeToLiveSeconds(0));
		config.getSerializationConfig().addDataSerializableFactory(ReverseGeoSerializableFactory.ID,
				new ReverseGeoSerializableFactory());
		return config;
	}

	@Bean
	public HazelcastInstance hazelcastInstance() {
		return Hazelcast.newHazelcastInstance(config()); // (2)
	}

	@Bean
	public CacheManager cacheManager() {
		HazelcastCacheManager hcm = new HazelcastCacheManager(hazelcastInstance()); // (3)
		return hcm;
	}
}
