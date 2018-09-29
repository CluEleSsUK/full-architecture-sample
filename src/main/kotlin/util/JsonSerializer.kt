package util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer

// in reality, I'd probably use Avro instead of JSON because schemas are nice :)
val mapper = ObjectMapper().registerKotlinModule()

class JsonSerializer<T>: Serializer<T> {

  override fun serialize(topic: String?, data: T): ByteArray {
    return mapper.writeValueAsBytes(data)
  }

  override fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {
    // no op
  }

  override fun close() {
    // no op
  }

}

class JsonDeserializer<T>: Deserializer<T> {
  override fun deserialize(topic: String?, data: ByteArray?): T {
    return mapper.readValue(data, object: TypeReference<T>() {})
  }

  override fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {
    // no op
  }

  override fun close() {
    // no op
  }
}
