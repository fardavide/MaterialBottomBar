function copyEnvVarsToLocalProperties {
    LOCAL_PROPERTIES=$HOME"/.gradle/local.properties"
    export LOCAL_PROPERTIES
    echo "Local Properties should exist at $LOCAL_PROPERTIES"
 
    if [ ! -f "$LOCAL_PROPERTIES" ]; then
        echo "Local Properties does not exist"
 
        echo "Creating Local Properties file..."
        touch $LOCAL_PROPERTIES
 
        echo "Writing bintray.user to local.properties..."
        echo "bintray.user=$bintrayUser" >> $LOCAL_PROPERTIES

        echo "Writing bintray.apikey to local.properties..."
        echo "bintray.apikey=$bintrayApikey" >> $LOCAL_PROPERTIES
    fi
}