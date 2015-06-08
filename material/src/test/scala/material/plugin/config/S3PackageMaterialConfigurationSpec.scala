package material.plugin.config

import com.indix.gocd.s3material.config.S3PackageMaterialConfiguration
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers._
import scala.collection.JavaConverters._
import com.thoughtworks.go.plugin.api.material.packagerepository.PackageMaterialProperty
import com.thoughtworks.go.plugin.api.response.validation.ValidationError

class S3PackageMaterialConfigurationSpec extends FlatSpec {
  "S3PackageMaterialConfiguration" should "return repository configuration" in {
    val repoConfig = new S3PackageMaterialConfiguration().getRepositoryConfiguration()
    val properties = repoConfig.list().asScala.map(_.asInstanceOf[PackageMaterialProperty])
    val keys = properties.map(_.getKey)
    keys should contain (S3PackageMaterialConfiguration.S3_BUCKET)
  }

  it should "validate repository configuration and return no errors if all configurations are valid" in {
    val configuration = new S3PackageMaterialConfiguration()
    val repoConfig = configuration.getRepositoryConfiguration()
    repoConfig.list().asScala.map(_.asInstanceOf[PackageMaterialProperty]).foreach{ p =>
      p.withDefault("default value")
    }
    configuration.isRepositoryConfigurationValid(repoConfig).getErrors.asScala should be(List[ValidationError]())
  }

  it should "validate repository configuration and return errors if there are invalid configurations" in {
    val configuration = new S3PackageMaterialConfiguration()
    val repoConfig = configuration.getRepositoryConfiguration()
    repoConfig.list().asScala.map(_.asInstanceOf[PackageMaterialProperty]).foreach{ p =>
      p.withDefault("   ")
    }
    val validationErrors = configuration.isRepositoryConfigurationValid(repoConfig).getErrors.asScala
    validationErrors.length should be(1)
  }

  it should "return package configuration" in {
    val packageConfig = new S3PackageMaterialConfiguration().getPackageConfiguration
    val keys = packageConfig.list().asScala.map(_.asInstanceOf[PackageMaterialProperty]).map(_.getKey)
    keys.length should be(4)
    keys should contain (S3PackageMaterialConfiguration.PATH_NAME)
    keys should contain (S3PackageMaterialConfiguration.PIPELINE_NAME)
    keys should contain (S3PackageMaterialConfiguration.JOB_NAME)
    keys should contain (S3PackageMaterialConfiguration.STAGE_NAME)
  }

}
