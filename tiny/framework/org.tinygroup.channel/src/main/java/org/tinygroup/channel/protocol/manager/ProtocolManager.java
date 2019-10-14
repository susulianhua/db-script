/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//package org.tinygroup.channel.protocol.manager;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.tinygroup.beancontainer.BeanContainerFactory;
//import org.tinygroup.channel.protocol.ProtocolInInterface;
//import org.tinygroup.channel.protocol.ProtocolInterface;
//import org.tinygroup.channel.protocol.ProtocolOutListener;
//import org.tinygroup.channel.protocol.ProtocolOutInterface;
//import org.tinygroup.channel.protocol.config.ProtocolConfig;
//import org.tinygroup.channel.protocol.config.ProtocolListenerConfig;
//
//@SuppressWarnings({ "rawtypes", "unchecked" })
//public class ProtocolManager extends ProtocolConfigManager {
//	private Map<String, ProtocolInInterface> inList = new HashMap<String, ProtocolInInterface>();
//	private Map<String, ProtocolOutInterface> outList = new HashMap<String, ProtocolOutInterface>();
//
//	public void initProtocols() {
//		if (getProtocolConfigs() == null) {
//			return;
//		}
//		for (ProtocolConfig config : getProtocolConfigs().getConfigs()) {
//			initProtocol(config);
//		}
//	}
//
//	private void initProtocol(ProtocolConfig config) {
//		String bean = config.getBean();
//		ProtocolInterface protocolInterface = BeanContainerFactory
//				.getBeanContainer(ProtocolManager.class.getClassLoader())
//				.getBean(bean);
//		protocolInterface.setId(config.getId());
////		protocolInterface.initParams(toMap(config.getParams()));
//		initProtocol(protocolInterface, config.getListeners());
//		String type = config.getType();
//		if(ProtocolConfig.TYPE_IN.equals(type)){
//			inList.put(config.getId(), (ProtocolInInterface) protocolInterface);
//		}else if(ProtocolConfig.TYPE_OUT.equals(type)){
//			outList.put(config.getId(),  (ProtocolOutInterface) protocolInterface);
//		}
//	}
//
//	private void initProtocol(ProtocolInterface protocolInterface,
//			List<ProtocolListenerConfig> listenerConfigs) {
//		for (ProtocolListenerConfig listenerConfig : listenerConfigs) {
//			protocolInterface
//					.addProtocolListener(initProtocolListener(listenerConfig));
//		}
//
//	}
//
//	private ProtocolOutListener initProtocolListener(
//			ProtocolListenerConfig listenerConfig) {
//		String bean = listenerConfig.getBean();
//		ProtocolOutListener protocolListener = BeanContainerFactory
//				.getBeanContainer(ProtocolManager.class.getClassLoader())
//				.getBean(bean);
//		protocolListener.setId(listenerConfig.getId());
//		return protocolListener;
//
//	}
//
////	private Map<String, String> toMap(List<ParamConfig> paramConfigs) {
////		Map<String, String> params = new HashMap<String, String>();
////		for (ParamConfig config : paramConfigs) {
////			params.put(config.getName(), config.getValue());
////		}
////		return params;
////	}
//
//	
//
//	ProtocolInterface getProtocolInterfaceById(String id){
//		return inList.get(id);
//	}
//	ProtocolOutInterface getProtocolOutInterfaceById(String id){
//		return outList.get(id);
//	}
//}