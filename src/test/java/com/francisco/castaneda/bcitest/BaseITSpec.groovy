package com.francisco.castaneda.bcitest

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import spock.lang.Specification


@AutoConfigureWireMock(port = 0)
@SpringBootTest(classes = [BciTestApplication])
abstract class BaseITSpec extends Specification {
}
