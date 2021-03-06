/*
 * Copyright (C) 2012 tamtam180
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arangodb.util;

import com.arangodb.ArangoException;

/**
 * @author tamtam180 - kirscheless at gmail.com
 *
 */
public class ReflectionUtils {

	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<?> clazz) throws ArangoException {
		try {
			return (T) clazz.newInstance();
		} catch (InstantiationException e) {
			throw new ArangoException(e);
		} catch (IllegalAccessException e) {
			throw new ArangoException(e);
		}
	}

}
