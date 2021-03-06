/*
 * Copyright (C) 2015 ArangoDB GmbH
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

package com.arangodb.example.document;

import java.util.HashMap;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.arangodb.ArangoDriver;
import com.arangodb.ArangoException;
import com.arangodb.DocumentCursor;
import com.arangodb.entity.DocumentEntity;
import com.arangodb.util.AqlQueryOptions;

public class SimplePersonAqlQueryWithLimitExample extends BaseExample {

	private static final String DATABASE_NAME = "SimplePersonAqlQueryWithLimitExample";

	private static final String COLLECTION_NAME = "SimplePersonAqlQueryWithLimitExample";

	public ArangoDriver arangoDriver;

	@Before
	public void _before() {
		removeTestDatabase(DATABASE_NAME);

		arangoDriver = getArangoDriver(getConfiguration());
		createDatabase(arangoDriver, DATABASE_NAME);
		createCollection(arangoDriver, COLLECTION_NAME);
	}

	@Test
	public void simplePersonAqlWithLimitQuery() {
		//
		// You can find the ArangoDB Web interface here:
		// http://127.0.0.1:8529/
		//
		// change the log level to "debug" in /src/test/resource/logback.xml to
		// see the HTTP communication

		try {
			//
			printHeadline("create example documents");
			//
			createExamples();

			//
			printHeadline("build query");
			//

			// build an AQL query string
			String queryString = "FOR t IN " + COLLECTION_NAME + " FILTER t.gender == @gender LIMIT 5 RETURN t";
			System.out.println(queryString);

			// bind @gender to female
			HashMap<String, Object> bindVars = new HashMap<String, Object>();
			bindVars.put("gender", FEMALE);

			// query (count = true, batchSize = 5)
			AqlQueryOptions aqlQueryOptions = new AqlQueryOptions().setCount(true).setFullCount(true);

			//
			printHeadline("execute query");
			//

			DocumentCursor<SimplePerson> rs = arangoDriver.executeDocumentQuery(queryString, bindVars, aqlQueryOptions,
				SimplePerson.class);
			Assert.assertNotNull(rs);

			//
			printHeadline("get number of results");
			//

			// number of results with LIMIT = 5
			System.out.println("cursor count = " + rs.getCount());
			Assert.assertEquals(5, rs.getCount());

			// number of results without LIMIT
			System.out.println("cursor full count = " + rs.getFullCount());
			Assert.assertTrue(5 < rs.getFullCount());

			//
			printHeadline("print results");
			//

			// using the DocumentEntity iterator
			Iterator<DocumentEntity<SimplePerson>> iterator = rs.iterator();
			while (iterator.hasNext()) {
				DocumentEntity<SimplePerson> documentEntity = iterator.next();

				Assert.assertNotNull(documentEntity);
				Assert.assertNotNull(documentEntity.getDocumentKey());
				Assert.assertNotNull(documentEntity.getDocumentHandle());
				Assert.assertNotNull(documentEntity.getDocumentRevision());
				Assert.assertNotNull(documentEntity.getEntity());

				SimplePerson person = documentEntity.getEntity();
				System.out.printf("%20s  %15s(%5s): %d%n", documentEntity.getDocumentKey(), person.getName(),
					person.getGender(), person.getAge());
			}

		} catch (ArangoException e) {
			Assert.fail("Example failed. " + e.getMessage());
		}

	}

	private void createExamples() throws ArangoException {
		// create some persons
		for (int i = 0; i < 100; i++) {
			SimplePerson person = new SimplePerson();
			person.setName("TestUser" + i);
			person.setGender((i % 2) == 0 ? MALE : FEMALE);
			person.setAge((int) (Math.random() * 100) + 10);

			arangoDriver.createDocument(COLLECTION_NAME, person, true, null);
		}

	}

}
