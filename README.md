# pretty-print

### Annotation Processor to pretty print your Activity / Fragment extras.

### How to use
PrettyPrinterExtras will be the name of the generated class,header name(given in the annotation) with postfix of printExtras will be the name of the method for that Activity / Fragment.

For eg:

```java
@Pretty(headerName = "MainActivity")
public class MyActivity extends Activity {

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		int id = 123;
		String title = "test";		
		
		//call to pretty print extras.		
		PrettyPrinterExtras.MainActivityprintExtras(getIntent().getExtras());	
	}
}
```
and thats it, all the extras will be printed in your logcat as below.

```
╔═══════════╤════════════════╗
║ ClassName │ SecondActivity ║
╠═══════════╪════════════════╣
║ number    │ 2              ║
╟───────────┼────────────────╢
║ string    │ string value   ║
╟───────────┼────────────────╢
║ int       │ 12             ║
╟───────────┼────────────────╢
║ float     │ 12.0           ║
╟───────────┼────────────────╢
║ hello     │ text           ║
╟───────────┼────────────────╢
║ world     │ text           ║
╚═══════════╧════════════════╝ 

```

### Adding the dependency
Before starting you will need to add [fliptables](https://github.com/JakeWharton/flip-tables) dependency.By default pretty-print does not package this dependency. 

```groovy
implementation 'com.jakewharton.fliptables:fliptables:x.x.x'
```
For now you can add it as `maven` dependency, in future we will be including this in `JCenter`

```groovy
 maven { url 'https://dl.bintray.com/nullpointerguy/maven/' }
```

and finally we add pretty print as dependency.

```groovy
implementation 'com.karthik.pretty_annotation:pretty-annotation:0.4.1'
annotationProcessor 'com.karthik.pretty_compiler:pretty-compiler:0.4.1'
```
