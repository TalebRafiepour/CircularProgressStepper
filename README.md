# CircularProgressStepper
Rounded Android Progress Bar For Show Steps Of A Proccess

<img src="https://github.com/TalebRafiepour/CircularProgressStepper/blob/master/cps-gif.gif" width="300"> 

## 1.setup 
Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
  
  -------------------------------------

Add the dependency
```
  dependencies {
	        implementation 'com.github.TalebRafiepour:CircularProgressStepper:0.1'
	}
   
```
## 2.usage

```
<com.taleb.cps.CircularProgressStepper android:layout_width="300dp" android:layout_height="300dp"
                                           android:layout_gravity="center|top"
                                           android:id="@+id/cpsStepper"
                                           app:cps_numOfSteps="5"
                                           app:cps_currentStep="4"
                                           app:cps_stepsSize="38dp"
                                           app:cps_foregroundColor="#E4E4E4"
                                           app:cps_progressColor="#3FE6D1"
                                           app:cps_progressStrokeOffset="5dp"
                                           android:padding="10dp"/>
                                           ```
