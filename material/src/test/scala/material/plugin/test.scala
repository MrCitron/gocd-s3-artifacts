package material.plugin

import com.indix.gocd.s3material.config.S3PackageMaterialConfiguration
import com.indix.gocd.s3material.plugin.S3PackageMaterialPoller
import com.thoughtworks.go.plugin.api.material.packagerepository.{RepositoryConfiguration, PackageConfiguration}
import org.mockito.Mockito.when
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers._
import org.scalatest.mock.MockitoSugar
import com.thoughtworks.go.plugin.api.config.Property

class Test extends FlatSpec with MockitoSugar {

  "S3PackageMaterialPoller" should "poll package" in {

    val repoConfig = mock[RepositoryConfiguration]
    when(repoConfig.get(S3PackageMaterialConfiguration.S3_BUCKET)).thenReturn(new Property(S3PackageMaterialConfiguration.S3_BUCKET, "xw-builds", ""))

    val packageConfig = mock[PackageConfiguration]
    when(packageConfig.get(S3PackageMaterialConfiguration.PATH_NAME)).thenReturn(new Property(S3PackageMaterialConfiguration.PATH_NAME, "jobs/xw-catalog-services/", ""))

    val poller = new S3PackageMaterialPoller()

    assertResult(true) {
      poller.checkConnectionToRepository(repoConfig).isSuccessful
    }

    assertResult(true) {
      poller.checkConnectionToPackage(packageConfig, repoConfig).isSuccessful
    }

    assert(poller.getLatestRevision(packageConfig, repoConfig) != null)

  }

}
