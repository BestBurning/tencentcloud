# tencentcloud

在[e.coding.net](https://e.coding.net/)上用官方提供的[demo](https://codes-farm.coding.net/p/html-cos-demo/d/html-cos-demo/git)自动将`博客代码`推至**腾讯COS**
不过最后涉及云函数刷新CDN的地方，计费方式过于恐怖，所以用自己的方式去刷新CDN


## 前置步骤

直到`刷新CDN`之前与[demo](https://codes-farm.coding.net/p/html-cos-demo/d/html-cos-demo/git)保持一致，
之后在`coding`->`项目`中`构建`设置中添加一步`刷新CDN`:
![cdn](https://github.com/BestBurning/tencentcloud/blob/master/imgs/cdn.png)

```
pipeline {
  agent any
  stages {
    stage('检出') {
      steps {
        checkout([
          $class: 'GitSCM',
          branches: [[name: env.GIT_BUILD_REF]],
          userRemoteConfigs: [[url: env.GIT_REPO_URL, credentialsId: env.CREDENTIALS_ID]]
        ])
      }
    }
    stage('部署到腾讯云存储') {
      steps {
        echo '部署中...'
        sh 'coscmd config -a $TENCENT_SECRET_ID -s $TENCENT_SECRET_KEY -b $TENCENT_BUCKET -r $TENCENT_REGION'
        sh 'rm -rf .git'
        sh 'coscmd upload -r ./ /'
        echo '部署完成'
      }
    }
    stage('刷新CDN') {
      steps {
        echo '准备刷新'
        sh 'git clone https://github.com/BestBurning/tencentcloud.git'
        dir(path: './tencentcloud') {
          sh 'mvn clean package '
          sh 'java -jar ./target/tencentcloud-1.0-SNAPSHOT.jar $TENCENT_SECRET_ID $TENCENT_SECRET_KEY ap-guangzhou https://di1shuai.com/'
        }
        echo '刷新完毕'
      }
    }
  }
}
```
### 参数说明
```
java -jar ./target/tencentcloud-1.0-SNAPSHOT.jar $TENCENT_SECRET_ID $TENCENT_SECRET_KEY ap-guangzhou https://di1shuai.com/
```
1. 腾讯云`SECRET_ID`
2. 腾讯云`SECRET_KEY`
3. 区域
4. `>=4`的参数均为要刷新的`目录`