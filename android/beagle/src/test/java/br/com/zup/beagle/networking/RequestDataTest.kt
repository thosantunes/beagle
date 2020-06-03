/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.zup.beagle.networking

import br.com.zup.beagle.enums.BeaglePlatform
import br.com.zup.beagle.testutil.RandomData
import org.junit.Before
import org.junit.Test
import java.net.URI
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RequestDataTest {

    private lateinit var requestData: RequestData

    @Before
    fun setUp() {
        requestData = RequestData(URI(RandomData.httpUrl()))
    }

    @Test
    fun requestData_should_have_method_GET_as_default() {
        assertEquals(HttpMethod.GET, requestData.method)
    }

    @Test
    fun requestData_should_have_just_beaglePlatform_header() {
        assertEquals(1, requestData.headers.size)
        assertEquals(BeaglePlatform.ANDROID.name, requestData.headers[RequestData.BEAGLE_PLATFORM_HEADER])
    }

    @Test
    fun requestData_should_have_data_null() {
        assertNull(requestData.body)
    }
}