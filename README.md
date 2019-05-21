[![](https://jitpack.io/v/ostamustafa/Sprint.svg)](https://jitpack.io/#ostamustafa/Sprint)

# Sprint
Sprint is a RESTful android java library

## Gradle:

### In project level gradle:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

### In app level gradle:

```gradle
dependencies {
    implementation 'com.github.ostamustafa:sprint:1.0.3'
}
```

## Usage:

### GET Example:

```java
Sprint.instance()
.request("http://some.url")
    .execute(new SprintCallback() {
        
        @Override
        public void onResponse(Response response) {
        
        }
        
        @Override
        public void onException(Exception e) {
        
        }
    });
```

### POST and other methods:

```java
Sprint.instance(Method.POST)
    .request("http://some.url", args)
    .execute(new SprintCallback() {
        
        @Override
        public void onResponse(Response response) {
        
        }
        
        @Override
        public void onException(Exception e) {
        
        }
    });
```
args can be `HashMap<String, String>` or `JSONObject` or `String`

__more information will be added later on__
