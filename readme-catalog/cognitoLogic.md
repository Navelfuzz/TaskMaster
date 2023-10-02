# Cognito Logic Notes

## This was the hardcoded logic for the cognito user pool

### Cognito Sign Up Logic
```java
Amplify.Auth.signUp("jcstillson13@gmail.com", "Password*",
        AuthSignUpOptions.builder()
        .userAttribute(AuthUserAttributeKey.email(), "jcstillson13@gmail.com")
        .userAttribute(AuthUserAttributeKey.nickname(), "Jon")
        .build(),
        successResponse -> Log.i(TAG, "Sign up succeeded: " + successResponse.toString()),
        failureResponse -> Log.i(TAG, "Sign up failed with username: " + "jcstillson13@gmail.com" + "with this message: " + failureResponse.toString())
);
```
        
### Cognito Verification Logic
```java
Amplify.Auth.confirmSignUp("jcstillson13@gmail.com",
        "364496",
        success -> {
        Log.i(TAG, "Verification succeeded: " + success.toString());
        },
        failure -> {
        Log.i(TAG, "Verification failed: " + failure.toString());
        }
);
```
        
### Cognito Sign In Logic
```java
Amplify.Auth.signIn("jcstillson13@gmail.com", "Password*",
    success -> Log.i(TAG, "Login Succeeded: " + success.toString()),
    failure -> Log.i(TAG, "Login Failed: " + failure.toString())
);
```



### Cognito Sign Out Logic
```java
AuthSignOutOptions signOutOptions = AuthSignOutOptions.builder()
        .globalSignOut(true)
        .build();

        Amplify.Auth.signOut(signOutOptions,
        signOutResult -> {
        if(signOutResult instanceof AWSCognitoAuthSignOutResult.CompleteSignOut){
        Log.i(TAG, "Global sign out successful!");
        } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.PartialSignOut){
        Log.i(TAG, "Partial sign out successful!");
        } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.FailedSignOut){
        Log.i(TAG, "Sign out failed: " + signOutResult.toString());
        }
        }
        );
```

### Set the user's username based on their nickname saved in the Cognito user pool

```java
Amplify.Auth.fetchUserAttributes(
          success -> {
            for(AuthUserAttribute userAttribute : success) {
              if(userAttribute.getKey().getKeyString().equals("nickname")) {
                String userNickname = userAttribute.getValue();
                runOnUiThread(() -> {
                  // update a textView with the nickname
                });
              }
            }
          },
          failure -> {

          }
        );
```
        

