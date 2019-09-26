package com.atguigu.myrule;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomRule_ZY extends AbstractLoadBalancerRule {

    /**
     * 总共被调用的次数，目前要求每台被调用5次
     */
    private int total= 0;
    /**
     * 当前提供服务的机器号
     */
    private int currentIndex= 0;
    /**
     * Randomly choose from all living servers
     */
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;

        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();
            List<Server> allList = lb.getAllServers();

            int serverCount = allList.size();
            if (serverCount == 0) {
                return null;
            }
            /*int index = chooseRandomInt(serverCount);
            server = upList.get(index);*/
            if(total<5){
                server=upList.get(currentIndex);
                total++;
            }else {
                total=0;
                currentIndex++;
                if(currentIndex>=upList.size()){
                    currentIndex=0;
                }

            }

            if (server == null) {
                Thread.yield();
                continue;
            }
            if (server.isAlive()) {
                return (server);
            }
            server = null;
            Thread.yield();
        }
        return server;

    }
    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }
}
