package org.tinygroup.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.tinygroup.config.util.ConfigurationUtil;

/**
 * 按目前的方案
 * 存放顺序为
 *  head(memory)
 * 	local
 * 	remote
 * 每项都使用
 * @author chenjiao
 *
 */
public class ConfigCurdManager {
	public final static String HEAD_KEY = "head";
	public final static String TAIL_KEY = "tail";
	
	Map<String, MapItem> configs = new HashMap<String, ConfigCurdManager.MapItem>();
	/**
	 * 为true时，   local/remote/memory，独立管理，独立触发事件，取值优先级为 remote>local>memory
	 * 若为false，local/remote/memory，独立管理，统一触发事件，取值优先级为 remote>local>memory
	 * 
	 * 若remote中存在kv对a=b,而memory中不存在a，此时对memory操作put(a,v)，
	 * 若为true,触发add事件,因为memory与remote是独立触发的
	 * 若为false,触发update事件,因为memory与remote是统一触发的
	 */
	boolean isConfigMap = true;
	Map<String, String> merageMap ;
	MapItem tail;
	MapItem head;// default;
	

	public ConfigCurdManager() {
		this(true);
	}
	public ConfigCurdManager(boolean isConfigMap){
		this.isConfigMap = isConfigMap;
		head = new MapItem(isConfigMap);
		tail = new MapItem(isConfigMap);
		head.next = tail;
		tail.pre = head;
		if(isConfigMap){
			merageMap = new HashMap<String, String>();
		}else{
			merageMap = new ConfigMap();
		}
	}

	private void putMapItem(String name, MapItem mapitem) {
		configs.put(name, mapitem);
		configsChange();
	}

	private void configsChange() {
		computeTempMap();
	}

	protected void computeTempMap() {
		if(isConfigMap){
			configMapMod();
		}else{
			notconfigMapMod();
		}
		
	}

	private void notconfigMapMod() {
		Map<String, String> tempStore = new HashMap<String, String>();
		MapItem item = head;
		while( item!=null ){
			//除tail将所有map合并至tempMap2
			merage(tempStore,new HashMap<String, String>(item.getMap()));
			item = item.next;
		}
		tempStore = ConfigurationUtil.replace(tempStore);
		Map<String,String> tempMap3  = ((ConfigMap)merageMap).clone();//不会触发事件
		compareAndUpdate(tempMap3, tempStore); //触发事件
		merageMap = tempMap3;
	}

	private void merage(Map<String, String> tempStore, HashMap<String, String> hashMap) {
		tempStore.putAll(hashMap);
	}
	private void configMapMod() {
		Map<String, String> tempMap = new HashMap<String, String>();
		MapItem item = head;
		while (item != null) {
			tempMap.putAll(item.getMap());
			item = item.next;
		}
		merageMap = tempMap;
	}
	
	/**
	 * 在队尾加入Map
	 * @param name 新Map的名称
	 * @param map 
	 */
	public void addLast(String name, Map<String, String> map) {
		if(checkDuplicateName(name, map)){
			return ;
		}
		synchronized (this) {
			//checkDuplicateName(name);
			addBefore(tail, name, map);
		}
	}

	/**
	 * 在队首加入Map
	 * @param name 新Map的名称
	 * @param map 
	 */
	public void addFirst(String name, Map<String, String> map) {
		if(checkDuplicateName(name, map)){
			return ;
		}
		synchronized (this) {
			//checkDuplicateName(name);
			addAfter(head, name, map);
		}
	}

	/**
	 * 在目标Map后加入Map
	 * @param targetName 目标Map
	 * @param name 新Map的名称
	 * @param map 
	 */
	public void addAfter(String targetName, String name, Map<String, String> map) {
		if(checkDuplicateName(name, map)){
			return ;
		}
		synchronized (this) {
			MapItem targetItem = configs.get(targetName);
			if (targetItem == null) {
				throw new RuntimeException("not found target Map :" + targetName);
			}
			//checkDuplicateName(name);
			addAfter(targetItem, name, map);
		}
	}
	
	private boolean checkDuplicateName(String name,Map<String, String> map) {
		if (configs.containsKey(name)) {
			replace(name,map);
			return true;
		}
		return false;
	}
	
	public boolean ifExistTargetMapItem(String name){
		return configs.containsKey(name);
	}

//	private void checkDuplicateName(String name) {
//		if (configs.containsKey(name)) {
//			throw new RuntimeException("Duplicate Map name: " + name);
//		}
//	}

	private void addAfter(MapItem targetItem, String name, Map<String, String> map) {
		MapItem mapitem = new MapItem(map,isConfigMap);
		MapItem oldAfter = targetItem.next;
		oldAfter.pre = mapitem;
		mapitem.next = oldAfter;
		mapitem.pre = targetItem;
		targetItem.next = mapitem;
		putMapItem(name, mapitem);
	}

	/**
	 * 在目标Map前加入Map
	 * @param targetName 目标Map
	 * @param name 新Map的名称
	 * @param map 
	 */
	public void addBefore(String targetName, String name, Map<String, String> map) {
		if(checkDuplicateName(name, map)){
			return ;
		}
		synchronized (this) {
			MapItem targetItem = configs.get(targetName);
			if (targetItem == null) {
				throw new RuntimeException("not found target Map :" + targetName);
			}
			//checkDuplicateName(name);
			addBefore(targetItem, name, map);
		}

	}

	private void addBefore(MapItem targetItem, String name, Map<String, String> map) {
		MapItem mapitem = new MapItem(map,isConfigMap);
		MapItem oldpre = targetItem.pre;
		oldpre.next = mapitem;
		mapitem.pre = oldpre;
		mapitem.next = targetItem;
		targetItem.pre = mapitem;
		putMapItem(name, mapitem);
	}

	/**
	 * 替换目标Map
	 * @param name 被替换的Map的名称
	 * @param map 
	 */
	public void replace(String name, Map<String, String> map) {
		synchronized (this) {
			MapItem targetItem = configs.get(name);
			if (targetItem == null) {
				throw new RuntimeException("not found target Map :" + name);
			}
			replace(targetItem, name, map);
		}

	}

	private void replace(MapItem targetItem,String name, Map<String, String> dest) {
		Map<String, String> orMap = targetItem.getMap();
		Map<String, String> cloneOrMap = orMap;
		if(orMap instanceof  ConfigMap){
			cloneOrMap = ((ConfigMap)orMap).clone();//使用clone,不触发事件put
		}else{
			cloneOrMap = new HashMap<String, String>(orMap);
		}
		Map<String, String> temp = new HashMap<String, String>(dest);
		compareAndUpdate(cloneOrMap,temp);//将map合并到cloneOrMap
		targetItem.setMap(cloneOrMap);
		putMapItem(name,targetItem);
	}
	
	/**
	 * 将dest合并至source
	 * @param source
	 * @param dest
	 */
	private void compareAndUpdate(Map<String, String> source, Map<String, String> dest) {
		Iterator<Entry<String, String>> it = source.entrySet().iterator();
		List<String> deleteKeys = new ArrayList<String>();
		while(it.hasNext()){
			Entry<String, String> entry = it.next();
			String key = entry.getKey();
			if (!dest.containsKey(key)) {
				deleteKeys.add(key);
				continue;
			}
			String newValue = dest.remove(key);
			String oldValue = source.get(key);
			if (!compare(oldValue, newValue)) {
				source.put(key, newValue);
			}
		}
	
		for (String key : dest.keySet()) {
			source.put(key, dest.get(key));
		}
		for (String key : deleteKeys){
			source.remove(key);
		}
	}

	private boolean compare(String value, String oldValue) {
		if (oldValue == null) {
			if (value == null) {
				return true;
			}
			return false;
		}
		return oldValue.equals(value);
	}
	
	public void removeMapItem(String name) {
		synchronized (this) {
			MapItem removeItem = configs.remove(name);
			if(removeItem != null){
				MapItem preItem = removeItem.pre;
				MapItem nextItem = removeItem.next;
				preItem.next = nextItem;
				nextItem.pre = preItem;
			    configsChange();
			}
		}
	}

	class MapItem {
		Map<String, String> map;
		MapItem pre;
		MapItem next;

		public MapItem(boolean useConfigMap) {
			this(new HashMap<String, String>(),useConfigMap);
		}

		public void setMap(Map<String, String> cloneOrMap) {
			map = cloneOrMap;
		}


		public MapItem(Map<String, String> map2, boolean useConfigMap) {
			if(useConfigMap){
				map = new ConfigMap(map2);
			}else{
				map = new HashMap<String, String>(map2);
			}
		}



		public Map<String, String> getMap() {
			return map;
		}
		

		public boolean containsKey(String key) {
			return map.containsKey(key);
		}
		
		public void clear(){
			map.clear();
			map = null;
			pre = null;
			next = null;
		}
	}


}
