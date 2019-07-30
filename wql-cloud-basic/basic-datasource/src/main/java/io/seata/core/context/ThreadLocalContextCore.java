///*
// *  Copyright 1999-2019 Seata.io Group.
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *       http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// */
//package io.seata.core.context;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import io.seata.common.loader.LoadLevel;
//
///**
// * The type Thread local context core.
// *
// * @author jimin.jm @alibaba-inc.com
// */
//@LoadLevel(name = "ThreadLocalContextCore", order = Integer.MIN_VALUE)
//public class ThreadLocalContextCore implements ContextCore {
//
//	/**
//	 * Hystrix会将请求放入Hystrix的线程池中去执行，此时会新启一个子线程处理请求。
//		而RootContext采用threadlocal传递数据，无法在子线程中传递，造成在子线程中通过RootContext.getXid()获取为空。
//		解决办法，在ThreadLocalContextCore类中，用InheritableThreadLocal 替代ThreadLocal。
//	 */
//    private ThreadLocal<Map<String, String>> threadLocal = new InheritableThreadLocal<Map<String, String>>() {
//        @Override
//        protected Map<String, String> initialValue() {
//            return new HashMap<String, String>();
//        }
//
//    };
//
//    @Override
//    public String put(String key, String value) {
//        return threadLocal.get().put(key, value);
//    }
//
//    @Override
//    public String get(String key) {
//        return threadLocal.get().get(key);
//    }
//
//    @Override
//    public String remove(String key) {
//        return threadLocal.get().remove(key);
//    }
//}
