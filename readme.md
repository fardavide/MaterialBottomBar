# Material Bottom bar

[![Download](https://api.bintray.com/packages/4face/MaterialBottomBar/studio.forface.materialbottombar/images/download.svg)](https://bintray.com/4face/MaterialBottomBar/studio.forface.materialbottombar/_latestVersion)  ![MinSDK](https://img.shields.io/badge/MinSDK-16-%23f44336.svg)  [![star this repo](http://githubbadges.com/star.svg?user=4face-studi0&repo=MaterialBottomBar&style=flat&color=fff&background=4caf50)](https://github.com/4face-studi0/MaterialBottomBar)  [![fork this repo](http://githubbadges.com/fork.svg?user=4face-studi0&repo=MaterialBottomBar&style=flat&color=fff&background=4caf50)](https://github.com/4face-studi0/MaterialBottomBar/fork)

**Material Bottom Bar** is an Android library that let you implement a customized **BottomAppBar** and a **bottom drawer** with just few simple steps.

#### Dsl ready

#### Navigation Component ready

![Demo application](https://media.giphy.com/media/28HvTGKfBYIYwmjDHb/giphy.gif)         ![WhatsBook](https://media.giphy.com/media/1jaIC1yKNaikPyktaU/giphy.gif)

## Wiki

### [Full wiki](https://github.com/4face-studi0/MaterialBottomBar/wiki)

## Docs

### [MaterialBottomBar Doc](https://4face-studi0.github.io/MaterialBottomBar/materialbottombar/)

### [Navigation Doc](https://4face-studi0.github.io/MaterialBottomBar/navigation/)

## Setup

Add the library:

```groovy
implementation "studio.forface.materialbottombar:materialbottombar:$version"
```

Add the optional navigation extension:

```groovy
implementation "studio.forface.materialbottombar:materialbottombar-navigation:$version"
```

## Getting started

### Layout ( minimal )

First you need to use a `MaterialBottomDrawerLayout` as parent of `MaterialBottomAppBar`.

```xml
<studio.forface.materialbottombar.layout.MaterialBottomDrawerLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/drawerLayout"   	    
    android:layout_width="match_parent"    
    android:layout_height="match_parent">
	
	<!-- Here it goes your content -->
    
    <studio.forface.materialbottombar.appbar.MaterialBottomAppBar
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"/>
	    
</studio.forface.materialbottombar.layout.MaterialBottomDrawerLayout>
```

More info about [**MaterialBottomDrawerLayout**](https://github.com/4face-studi0/MaterialBottomBar/wiki/MaterialBottomDrawerLayout) and [**MaterialBottomAppBar**](https://github.com/4face-studi0/MaterialBottomBar/wiki/MaterialBottomAppBar)

### Drawer ( minimal )

##### with DSL

```kotlin
drawerLayout.drawer = drawer {
    header {
        iconUrl = IMAGE_URL
        titleText = "My drawer"
    }
    body {
        onItemClick = { id, title -> /* do something */ }
        primaryItem( "Messages" ) {
            id = 1
            iconResource = R.drawable.ic_message_black_24dp
        }
        primaryItem( contactsSpannable ) {
            id = 2
            iconDrawable = contactsDrawable
        }
        divider()
        primaryItem( R.string.settings_drawer_title ) {
            id = 3
            iconBitmap = settingsBitmap
        }
    }
}
```

**without DSL:**

```kotlin
val header = MaterialDrawer.Header()
    .iconUrl( IMAGE_URL )
    .titleText( "My drawer" )
	    
val messages = PrimaryDrawerItem()
    .id( 1 )	
    .titeText( "messages" )
    .iconResource( R.drawable.ic_message_black_24dp )

val contacts = PrimaryDrawerItem()
    .id( 2 )
    .titleSpannable( contactsSpannable )
    .iconDrawable( contactsDrawable )

val settings = PrimaryDrawerItem()
    .id( 3 )
    .titleRes( R.string.settings_drawer_title )
    .iconBitmap( settingsBitmap )

val body = MaterialDrawer.Body()
    .itemClikListener { id, title -> /* do something */ }
    .items( listOf(
        messages,
        contacts,
        Divider(),
        settings
    ) ) 

drawerLayout.drawer = MaterialDrawer( header, body )
```

More info about [**Drawer and Panels**](https://github.com/4face-studi0/MaterialBottomBar/wiki/Drawer-and-Panels)

## Android Navigation Components integration

##### with DSL ( constructor injected NavController )

```kotlin
navDrawer( navController ) {
    header { ... }
    body {
        primaryItem( "messages" ) {
            navDestinationId = myDestinationId
            navDestinationBundle = myBundle
        }
        primaryItem( "contacts" ) {
            navDestination( myDestinationId ) { mapOf( "arg1" to 15 ) }
        }
        secondaryItem( "favorites" ) {
            navDirections = myNavDirections
        }
    }
}
```

##### NavController can also be set later

```kotlin
navDrawer {
    ...
}
```

#####  in the following ways:

* `myNavDrawer.navController = navController`
* `myToolbar.setupWithNavController( navController, myNavDrawer, myNavPanel1, myNavPanel2 )`
* `myMaterialBottomDrawerLayout.setupWithNavController( navController )`

##### without DSL

```kotlin
val header = MaterialDrawer.Header()
    .iconUrl( IMAGE_URL )
    .titleText( "My drawer" )

val messages = PrimaryNavDrawerItem()
    .titeText( "messages" )
    .navDestination( myDestinationId )
    .navDestinationBundle( myBundle )
    
val contacts = PrimaryNavDrawerItem()
    .titeText( "contacts" )
    .navDestination( myDestinationId ) { mapOf( "arg1" to "hello" ) }

val body = MaterialNavPanel.Body()
    .itemClickListener { id, title -> /* do something */ }
    .items( listOf( messages, contacts ) )

drawerLayout.drawer = MaterialNavDrawer( header, body )
```

## TIPS

The lib is built on 3 focal points:
* **Customization**
* **Easiness**
* **Compactness**

The first two are pretty self-explanatory, while the third one means that you should be able to write your fully customized Drawer with the less lines of code as possible; in other word is suggested to use an approach like this:

**with DSL:**

```kotlin
drawer {
    header { ... }
    body {
        val myBadge = Badge {
            backgroundCornerRadiusDp = 999f // Round badge
            backgroundColorRes = R.color.color_badge_background
            contentColorRes = R.color.color_badge_content
        }
        allPrimary { 
            badgeItem = myBadge 
            titleSizeSp = 20f
            titleColor = Color.RED
        }
        primaryItem( "First" ) { badgeContentText = "1" " }
        primaryItem( "Second" ) { /* No badge content, so the badge will be hidden */ }
    }
}
```

**withou DSL:**

```kotlin
val myBadge = BadgeItem()
    .backgroundCornerRadiusDp( 999f ) // Round badge
    .backgroundColorRes( R.color.color_badge_background )
    .contentColorRes( R.color.color_badge_content )

class MyPrimaryDrawerItem() { init {
    badge( myBadge )
    titleSizeSp( 20f )
    titleColor( Color.RED )
} }

val firstItem = MyPrimaryDrawerItem()
    .titleText( "First")
    .badgeContentText( "1" )

val secondItem = MyPrimaryDraweItem()
    .titleText( "Second" )
    // No badge content, so the badge will be hidden.
```

