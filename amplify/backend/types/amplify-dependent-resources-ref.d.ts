export type AmplifyDependentResourcesAttributes = {
  "analytics": {
    "taskmasterAnalytics": {
      "Id": "string",
      "Region": "string",
      "appName": "string"
    }
  },
  "api": {
    "temptaskmaster": {
      "GraphQLAPIEndpointOutput": "string",
      "GraphQLAPIIdOutput": "string",
      "GraphQLAPIKeyOutput": "string"
    }
  },
  "auth": {
    "temptaskmaster0b84a7f5": {
      "AppClientID": "string",
      "AppClientIDWeb": "string",
      "IdentityPoolId": "string",
      "IdentityPoolName": "string",
      "UserPoolArn": "string",
      "UserPoolId": "string",
      "UserPoolName": "string"
    }
  },
  "predictions": {
    "speechGeneratora423a233": {
      "language": "string",
      "region": "string",
      "voice": "string"
    },
    "translateText644fb09e": {
      "region": "string",
      "sourceLang": "string",
      "targetLang": "string"
    }
  },
  "storage": {
    "taskmasterStorage": {
      "BucketName": "string",
      "Region": "string"
    }
  }
}