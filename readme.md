# Material Bottom bar
**Material Bottom Bar** is an Android library that let you implement a customized **BottomAppBar** and a **bottom drawer** with just few simple steps.

Actually the library is in pre-alpha and I basically want to read your feedbacks and suggestions.

##### DO NOT USE IN PRODUCTION!

## Installation
Add the library to your Gradle dependecies:

    implementation "studio.forface.materialbottombar:materialbottombar:0.0.6"

## Usage
### Basic usage ( more complex example and TIPS below )
First you need to create a _layout_:

    <studio.forface.bottomappbar.layout.MaterialBottomDrawerLayout
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    android:id="@+id/drawerLayout"
   	    android:layout_width="match_parent"    
	    android:layout_height="match_parent">
    
	    <!-- Here it goes your content -->
	    
	    <studio.forface.bottomappbar.appbar.MaterialBottomAppBar
		    android:layout_width="match_parent"
		    android:layout_height="wrap_content"
		    android:layout_gravity="bottom"/>
		    
	</studio.forface.bottomappbar.layout.MaterialBottomDrawerLayout>

Then add your code:

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

### Complex usage ( see demo app for more details )
    <studio.forface.bottomappbar.layout.MaterialBottomDrawerLayout
		xmlns:android="http://schemas.android.com/apk/res/android"
		xmlns:app="http://schemas.android.com/apk/res-auto"
        
    	android:id="@+id/drawerLayout"
    	android:layout_width="match_parent"    
    	android:layout_height="match_parent">
        
    	<!-- Here it goes your content -->
	    
	    <studio.forface.bottomappbar.appbar.MaterialBottomAppBar
		    android:id="@+id/bar"
		    android:layout_width="match_parent"
		    android:layout_height="wrap_content"
		    android:layout_gravity="bottom"
		    
		    app:hideOnScroll="true"
		    app:hideFabOnScroll="true"
		    
		    app:backgroundTint="@android:color/holo_red_light"
		    app:navigationIcon="@drawable/ic_menu_black_24dp"
		    
		    app:fabAlignmentMode="center"
		    app:fabCradleRoundedCornerRadius="16dp"
		    app:leftCornerRadius="24dp"
		    app:leftCornerStyle="round"
		    app:rightCornerRadius="24dp"
		    app:rightCornerStyle="round"/>
	    
	    <com.google.android.material.floatingactionbutton.FloatingActionButton
		    android:layout_width="wrap_content"
		    android:layout_height="wrap_content"
		    app:layout_anchor="@id/bar"/>    
	    
	    </studio.forface.bottomappbar.layout.MaterialBottomDrawerLayout>

Code:

    private val testDrawer: MaterialDrawer get() {
    
	    val header = MaterialDrawer.Header()
			    .iconUrl( IMAGE_URL )
			    .backgroundColor( Color.RED )
			    .titleText( "Simple drawer" )
			    .titleColor( Color.WHITE )
			    .titleBold()
	    
	    val chat = PrimaryDrawerItem()
			    .titleText( "Chat" )
			    .withTitleSpSize(10f )
			    .titleColor( Color.MAGENTA )
			    .iconResource( R.drawable.ic_message_black_24dp )
			    .id( 1 )
			    .badgeBackgroundColor( Color.CYAN )
	    	    .badgeBackgroundCornerRadiusDp( 999f )
	    	    .badgeContentText( "Banana" )
	    	    .badgeContentBold()
	    	    .badgeContentColor( Color.BLACK )
	    	    .buttonContentText( "Edit" )
	    	    .buttonContentColor( Color.BLUE )
	    	    .buttonBackgroundColor( Color.BLUE )
	    	    .buttonBackgroundCornerRadiusDp( 999f )
	    	    .buttonStyle( ButtonStyle.FLAT )
	    
	    val inbox = PrimaryDrawerItem()
	    	    .titleText( "Inbox" )
	    	    .titleBold()
	    	    .iconResource( R.drawable.ic_inbox_black_24dp )
	    	    .id( 2 )
	    	    .badgeBackgroundColor( Color.TRANSPARENT )
	    	    .badgeBackgroundCornerRadiusDp( 999f )
	    	    .badgeContentText( "Banana" )
	    	    .badgeContentSpSize(14f )
	    	    .badgeContentColor( Color.GREEN )
	    	    .buttonContentText( "Edit" )
	    	    .buttonContentColor( Color.BLACK )
	    	    .buttonContentSpSize( 8f )
	    	    .buttonContentBold()
	    	    .buttonBackgroundColor( Color.RED )
	    	    .buttonBackgroundCornerRadiusDp( 10f )
	    	    .buttonStyle( ButtonStyle.FLAT )
	    
	    val work = SecondaryDrawerItem()
	    	    .titleText( "Work" )
	    	    .iconResource( R.drawable.ic_work_black_24dp )
	    	    .id( 3 )
	    	    .badgeBackgroundColor( Color.BLUE )
	    	    .badgeBackgroundCornerRadiusDp( 999f )
	    	    .badgeContentText( "3" )
	    	    .badgeContentSpSize(12f )
	    	    .badgeContentColor( Color.WHITE )
	    	    .buttonContentText( "Edit" )
	    	    .buttonContentColor( Color.WHITE )
	    	    .buttonContentSpSize( 8f )
	    	    .buttonContentBold()
	    	    .buttonBackgroundColor( Color.RED )
	    	    .buttonBackgroundCornerRadiusDp( 0f )
	    	    .buttonStyle( ButtonStyle.COLOR )
	    
	    val contacts = PrimaryDrawerItem()
	    	    .titleText( "Contacts" )
	    	    .titleBold()
	    	    .id( 4 )
	    	    .selectable( false )
	    	    .badgeBackgroundColor( Color.BLUE )
	    	    .badgeBackgroundCornerRadiusDp( 0f )
	    	    .badgeContentText( "3" )
	    	    .badgeContentBold()
	    	    .badgeContentColor( Color.WHITE )
	    
	    val favorites = SecondaryDrawerItem()
	    	    .titleText( "Favorites" )
	    	    .iconResource( R.drawable.ic_star_black_24dp )
	    	    .id( 5 )
	    
	    val labels = PrimaryDrawerItem()
	    	    .titleText( "Labels" )
	    	    .iconResource( R.drawable.ic_style_black_24dp )
	    	    .titleBold()
	    	    .id( 6 )
	    	    .badgeContentText( "12" )
	    
	    val label1 = SecondaryDrawerItem()
	    	    .titleText( "Label 1" )
	    	    .iconResource( R.drawable.ic_label_black_24dp )
	    	    .iconColor( Color.RED )
	    	    .id( 7 )
	    
	    val label2 = SecondaryDrawerItem()
	    	    .titleText( "Label 2" )
	    	    .iconResource( R.drawable.ic_label_black_24dp )
	    	    .iconColor( Color.GREEN )
	    	    .id( 8 )
	    	    .badgeContentText( "12" )
	    
	    val label3 = SecondaryDrawerItem()
	    	    .titleText( "Label 3" )
	    	    .iconResource( R.drawable.ic_label_black_24dp )
	    	    .iconColor( Color.BLUE )
	    	    
	    
	    val body = MaterialDrawer.Body()
	    	    .selectionColor( Color.BLUE )
	    	    .selectionCornerRadiusDp( 16f )
	    	    .items( listOf(
			    	    chat, inbox, work,
			    	    Divider(),
			    	    contacts, favorites,
			    	    Divider(),
			    	    labels, label1, label2, label3
	    	    ) )
	    	    .setSelected( 5 )
	    	    .itemClickListener { id, title ->
		    	    Toast.makeText(this, "$title - $id clicked", Toast.LENGTH_SHORT ).show()
	    	    }
	    
	    return MaterialDrawer( header, body ) 
	}

## TIPS
The lib is built on 3 focal points:
* **Customization**
* **Easiness**
* **Compactness**

The first two are pretty self-explanatory, while the third one means that you should be able to write your fully customized Drawer with the less lines of code as possible; in other word is suggested to use an approach like this:

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

