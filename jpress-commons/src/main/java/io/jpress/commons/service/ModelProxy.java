/**
 * Copyright (c) 2015-2022, Michael Yang Fuhai (fuhai999@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jpress.commons.service;

import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.CPI;
import io.jboot.db.model.Column;
import io.jboot.db.model.Columns;
import io.jboot.db.model.JbootModel;
import io.jboot.utils.ClassUtil;
import io.jpress.SiteContext;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyObject;

import java.lang.reflect.Method;

public class ModelProxy {

    public static <T> T get(Class<T> target) {
        javassist.util.proxy.ProxyFactory factory = new javassist.util.proxy.ProxyFactory();
        factory.setSuperclass(target);
        final Class<?> proxyClass = factory.createClass();


        T proxyObject = null;
        try {
            proxyObject = (T) proxyClass.newInstance();
            ((ProxyObject) proxyObject).setHandler(new ProcessColumnsHandler());
        } catch (Throwable e) {
            LogKit.error(e.toString(), e);
        }

        return proxyObject;
    }


    static class ProcessColumnsHandler implements MethodHandler {

        private static final String processColumns = "processColumns";
        private static final String copy = "copy";

        @Override
        public Object invoke(Object self, Method originalMethod, Method proxyMethod, Object[] args) throws Throwable {

            if (processColumns.equals(originalMethod.getName())) {
                Columns columns = (Columns) args[0];
                columns.addToFirst(Column.create("site_id", SiteContext.getSiteId()));
            }

            //copy
            else if (copy.equals(originalMethod.getName())) {
                JbootModel selfModel = (JbootModel) self;
                JbootModel dao = (JbootModel) get(ClassUtil.getUsefulClass(self.getClass()));
                dao.put(CPI.getAttrs(selfModel));
                return dao;
            }

            return proxyMethod.invoke(self, args);
        }

    }


}



