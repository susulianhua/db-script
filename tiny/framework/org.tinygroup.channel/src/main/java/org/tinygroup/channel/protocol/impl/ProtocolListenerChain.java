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
//package org.tinygroup.channel.protocol.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.tinygroup.channel.protocol.ProtocolOutListener;
//import org.tinygroup.context.Context;
//import org.tinygroup.logger.LogLevel;
//import org.tinygroup.logger.Logger;
//import org.tinygroup.logger.LoggerFactory;
//
//public class ProtocolListenerChain {
//	private final static Logger LOGGER = LoggerFactory.getLogger(ProtocolListenerChain.class);
//	private List<ProtocolOutListener<?,?>> list = new ArrayList<ProtocolOutListener<?,?>>();
//	private int size=0;
//	private int currentPrePosition = 0;
//	private int currentPostPosition = 0;
//	private int currentAfterTransPosition = 0;
//	public ProtocolListenerChain(List<ProtocolOutListener<?,?>> list) {
//		this.list = list;
//		if(list!=null&&!list.isEmpty()){
//			this.size = list.size();
//		}
//		
//	}
//	public void  afterTrans(Object request,Object protocolObject,Context context){
//		if (currentAfterTransPosition < size) {
//        	ProtocolOutListener nextLisenter = list.get(currentAfterTransPosition);
//        	LOGGER.logMessage(LogLevel.DEBUG, "firing ProtocolListener AFTERTRANS :'{}'", nextLisenter
//                    .getClass().getSimpleName());
//        	currentAfterTransPosition++;
//            nextLisenter.afterTrans(request,protocolObject,context,this);
//        }
//	}
//	
//	public void pre(Object request,Context context){
//        if (currentPrePosition < size) {
//        	ProtocolOutListener nextLisenter = list.get(currentPrePosition);
//        	LOGGER.logMessage(LogLevel.DEBUG, "firing ProtocolListener PRE :'{}'", nextLisenter
//                    .getClass().getSimpleName());
//        	currentPrePosition++;
//            nextLisenter.pre(request,context,this);
//        }
//    }
//	@SuppressWarnings("rawtypes")
//	public void post(Object request,Object response,Context context){
//        if (currentPostPosition < size) {
//        	ProtocolOutListener nextLisenter = list.get(currentPostPosition);
//        	LOGGER.logMessage(LogLevel.DEBUG, "firing ProtocolListener POST :'{}'", nextLisenter
//                    .getClass().getSimpleName());
//        	currentPostPosition++;
//            nextLisenter.post(request,response,context,this);
//        }
//    }
//	
//}
