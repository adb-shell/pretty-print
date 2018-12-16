# pretty-print    <img src="https://img.shields.io/badge/0.20-release-green.svg" alt="">

### Annotation to pretty print your Activity / Fragment bundle.

### How to use
Pretty print plugin will read the bundle parameter from any method annotated with `@Pretty`, and prints the bundle values and total size as shown in the below example.

For eg:

```java
    @Pretty(headerName = "MainActivity")
    private void intialize(Bundle extras) {
        //intialization.
    }
```

```kotlin
  @Pretty(headerName = "KotlinClass")
  private fun intialize(extras:Bundle) {
        //intialization.
   }
```

and all the extras will be printed in your logcat as below.

```
 ╔═══════════╤══════════════╗
 ║ ClassName │ KotlinClass  ║
 ╠═══════════╪══════════════╣
 ║ number    │ 2            ║
 ╟───────────┼──────────────╢
 ║ string    │ string value ║
 ╟───────────┼──────────────╢
 ║ int       │ 12           ║
 ╟───────────┼──────────────╢
 ║ float     │ 12.0         ║
 ╟───────────┼──────────────╢
 ║ hello     │ text         ║
 ╟───────────┼──────────────╢
 ║ world     │ text         ║
 ╟───────────┼──────────────╢
 ║ SIZE      │ 212 Bytes    ║
 ╚═══════════╧══════════════╝

```

### Adding the plugin dependency

#### 1.Add the classpath of the plugin as below
```groovy
 buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.com.karthik:pretty-plugin:0.2"
  }
}
```

#### 2.Apply the plugin in your project.
```groovy
apply plugin: "com.karthik.prettyprint"
```

#### TODO Feature in upcomming release:
1. - [ ] Printing Custom objects, not only primitives.

#### Credits
Pretty Print drew a huge inspiration from the Jake Wharton's Hugo and Can Elmas Let Libraries.
