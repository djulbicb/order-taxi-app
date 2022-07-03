package com.djulb.messages.redis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface RedisHelper<HK, T> {
    /**
     * Hash Structure Add Element* @param key * @param hashKey hashKey * @param domain element
     */
    void hashPut(String key, HK hashKey, T domain);
    /**
     * Hash Structure gets all key-value pairs for the specified key * @param key * @return
     */
    Map<HK, T> hashFindAll(String key);
    /**
     * Hash Structure gets a single element * @param key * @param hashKey * @return
     */
    T hashGet(String key, HK hashKey);
    void hashRemove(String key, HK hashKey);
    /**
     * List Structure adds elements to the tail (Right)* @param key * @param domain * @return
     */
    Long listPush(String key, T domain);
    /**
     * List Structure adds elements to the header (Left)* @param key * @param domain * @return
     */
    Long listUnshift(String key, T domain);
    /**
     * List Structure Gets All Elements* @param key * @return
     */
    List<T> listFindAll(String key);
    /**
     * List Structure removes and gets the first element of the array * @param key * @return
     */
    T listLPop(String key);
    /**
     * Entity class of object
     * @param key
     * @param domain
     * @return
     */
    void valuePut(String key, T domain);
    /**
     * Get Object Entity Class
     * @param key
     * @return
     */
    T getValue(String key);
    void remove(String key);
    /**
     * Set expiration time* @param key key* @param timeout time* @param timeUnit time unit
     */
    boolean expirse(String key, long timeout, TimeUnit timeUnit);
}