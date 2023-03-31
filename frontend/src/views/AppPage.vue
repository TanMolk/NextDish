<!--
 @Name AppPage
 @Description The app page
 @author Ashton & Wei
 @createDate 2023/03/12
-->
<template>
  <GoogleMap
      ref="mapInstance"
      class="map"
      :map-id="Constants.GOOGLE_MAP_ID"
      :api-key="Constants.GOOGLE_MAP_API_KEY"
      :zoom="14"
      :disable-default-ui="true"
  >
    <!----------------------Control-------------------->
    <CustomControl
        position="TOP_CENTER"
    >
      <ElButton
          v-show="directionModel"
          @click="
          directionModel = false;
          setMapCenter(userLocation);
          mapInstance.map.setZoom(14);
          "
          style="margin-top: 2em">
        Exit Direction
      </ElButton>
    </CustomControl>
    <!----------------------Control End---------------->


    <!----------------------Markers-------------------->

    <CustomMarker
        :options="userMarkerOption"
    >
      <img style="opacity: 0.7;width: 2em;height: 2em" src="@/assets/me.png" alt="me"/>
    </CustomMarker>
    <Circle
        :options="userCircleOption"
    />
    <Marker
        v-for="item in restaurantData.showingItems"
        :options="{
          label: item.name,
          position: item.geometry.location,
          title: item.rating + '',
          visible: item.place_id === this.restaurantData.detailPlaceId || !this.directionModel
        }"
        @click="markerClick(item)"
    />
    <!------------------Markers End-------------------->
  </GoogleMap>
  <CuisineTypeSelect
      class="cuisine-select-wrapper"
      @option-change="selectOptionChange"
  />
  <CuisineWindow
      :key="listDetailKey"
      ref="cuisineList"
      v-show="listShowState"
      :map-instance="mapInstance"
      :items="restaurantData.showingItems"
      :place-id="restaurantData.detailPlaceId"
      :list-loading-state="listDetailWindowStates.windowShow"
      :list-no-more-state="listDetailWindowStates.noMoreData"
      class="app-cuisine-list"
      @click-X="this.listShowState = false"
      @place-id-change="onCuisineListChangePlaceId($event)"
      @direction-request="onDirectionRequest($event)"
      @load-more-data="showMoreRestaurants"
  />
</template>

<script>
import {GoogleMap, Marker, Circle, CustomMarker, CustomControl} from 'vue3-google-map'
import {mileToMeter} from "@/utils/MileUtil";
import CuisineTypeSelect from "@/components/CuisineTypeSelect.vue";
import CuisineWindow from "@/components/CuisineWindow.vue";
import Constants from "@/constants/Constants";
import GoogleMapPlaceService from "@/service/GoogleMapPlaceService";
import {ElMessageBox} from 'element-plus';
import StorageUtil from "@/utils/StorageUtil";

export default {
  name: "AppPage",
  components: {CuisineWindow, CuisineTypeSelect, GoogleMap, Marker, CustomMarker, Circle, CustomControl},
  computed: {
    Constants() {
      return Constants
    }
  },
  watch: {
    directionModel(newVar) {
      //if in direction model
      if (newVar) {

      } else {
        //remove direction
        this.directionRender.setDirections({routes: []});
      }
    }
  },
  data() {
    return {
      // To store the Google map instance
      mapInstance: null,
      //render of direction
      directionRender: null,
      // user current location
      userLocation: {
        lat: 55,
        lng: -1.6,
      },
      // restaurant data set
      restaurantData: {
        //items of showing restaurants
        showingItems: [],
        //items of all restaurants
        allItems: [],
        //data of the detail windows
        detailPlaceId: null
      },
      mapOption: {
        zoom: 14,
      },
      //user marker option
      userMarkerOption: {
        position: this.userLocation,
        visible: this.ifInitialized
      },
      //1-mile circle option
      userCircleOption: {
        center: this.userLocation,
        radius: mileToMeter(1),
        strokeColor: '#d7dbdf',
        visible: this.ifInitialized
      },
      //flag of the page initialized state
      ifInitialized: false,
      //the flag of showing the list/detail window
      listShowState: false,
      //if user use direction
      directionModel: false,
      //loadingState
      listDetailWindowStates: {
        windowShow: false,
        noMoreData: false
      },
      //to force this component update by changing key
      listDetailKey: 0,
    }
  },
  methods: {
    /**
     * set the center of Map to user location
     * set condition to make sure mapInstance is not null
     * set time out to avoid calling this method before mapInstance is properly installed
     * @param location user location
     */
    setMapCenter(location) {
      setTimeout(() => {
        if (this.mapInstance && this.mapInstance.map) {
          this.mapInstance.map.panTo(location);
        }
      }, 50);
    },
    /**
     * IF UPDATE USER POSITION, MUST USE THIS METHOD!
     * IF CHANGE THE ATTRIBUTE DIRECTLY, NO CHANGE WILL HAPPEN.
     * MUST REPLACE THE OBJECT!!!!
     *
     * update the current user marker
     * contains user centre marker and the 1-mile circle
     *
     */
    freshUserMarkerOptions() {
      this.userMarkerOption = {
        position: this.userLocation,
        visible: this.ifInitialized
      }

      this.userCircleOption = {
        center: this.userLocation,
        radius: mileToMeter(1),
        strokeColor: '#d7dbdf',
        visible: this.ifInitialized
      }
    },
    /**
     * Update user current location
     */
    freshUserLocation() {
      //Check if support this feature
      if (navigator.geolocation) {
        //success callback function
        const successCallback = (position) => {
          //update user location
          this.userLocation = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
          }

          //If it doesn't initialize, firstly request restaurant data and set refer for mapInstance
          if (!this.ifInitialized) {
            this.loadRecentRestaurant("");
            this.ifInitialized = true;
            this.mapInstance = this.$refs.mapInstance;
            this.setMapCenter(this.userLocation);

            //register location watcher
            navigator.geolocation.watchPosition(successCallback, errorCallback);

            //close loading page
            this.$loading({
              fullscreen: true
            }).close();
          }
          this.freshUserMarkerOptions();
          //if it is in direction model
          if (this.directionModel) {
            this.setMapCenter(this.userLocation)
          }

          //store location for further usage
          StorageUtil.set(Constants.STORAGE_USER_LOCATION_LATITUDE, this.userLocation.lat)
          StorageUtil.set(Constants.STORAGE_USER_LOCATION_LONGITUDE, this.userLocation.lng)
          //If error
        }
        const errorCallback = (error) => {
          let errorMessage = '';
          switch (error.code) {
            case error.PERMISSION_DENIED:
              errorMessage = "Please allow location service in site setting"
              break;
            case error.POSITION_UNAVAILABLE:
              errorMessage = "Location information is unavailable"
              break;
            case error.TIMEOUT:
              errorMessage = "The request to get location timed out"
              break;
          }
          ElMessageBox.alert(errorMessage, 'Error');
        }

        //Set the callback function
        navigator.geolocation.getCurrentPosition(successCallback, errorCallback);
      }
    },
    /**
     * Control event when selection get change
     * @param type restaurant type
     */
    selectOptionChange(type) {
      this.loadRecentRestaurant(type);
      this.listShowState = true;
      //quit direction model
      this.directionModel = false;
      this.setMapCenter(this.userLocation);

      //call sub component method
      this.$refs.cuisineList.changeInfoWindow("list");
    },
    /**
     * Refresh nearby restaurant data set.
     * @param type Type of restaurant, like America, British
     */
    async loadRecentRestaurant(type) {
      this.listDetailWindowStates.windowShow = true;
      let response = await GoogleMapPlaceService.getRestaurantWithKeywordInOneMile(this.userLocation, type);

      //get all restaurants
      this.restaurantData.allItems = response.data.results;
      //render the first 10 restaurants
      this.restaurantData.showingItems = this.restaurantData.allItems.slice(0, 10);

      this.listDetailWindowStates.windowShow = false;

      //force update
      this.listDetailKey++
    },
    /**
     * What happens, if one restaurant marker get clicked
     * @param item Data of the marker
     */
    markerClick(item) {
      this.listShowState = true;
      this.setMapCenter(item.geometry.location);
      this.restaurantData.detailPlaceId = item.place_id;
      this.$refs.cuisineList.changeInfoWindow("detail", item);
      //quit direction model
      this.directionModel = false;
    },
    /**
     * when place-id-change event happens, the script runs.
     * @param data place_id
     */
    onCuisineListChangePlaceId(data) {
      this.restaurantData.detailPlaceId = data;
      //quit direction model
      this.directionModel = false;
    },
    /**
     * when direction-request event happens, the script runs.
     */
    onDirectionRequest(direction) {
      if (!this.directionRender) {
        this.directionRender = new this.mapInstance.api.DirectionsRenderer();
        this.directionRender.setMap(this.mapInstance.map)
      }
      this.directionRender.setDirections(direction);
      this.listShowState = false;
      this.directionModel = true;
    },
    showMoreRestaurants() {
      let allItems = this.restaurantData.allItems;
      let showingItems = this.restaurantData.showingItems;

      this.restaurantData.showingItems = allItems.slice(0, showingItems.length + 10);

      if (allItems.length === this.restaurantData.showingItems.length) {
        this.listDetailWindowStates.noMoreData = true;
      }
    }
  },
  /**
   * Event when all dom nodes get created
   */
  created() {
    //show loading
    this.$loading({
      fullscreen: true
    });
    //If it gets location successfully, the loading will disappear.
    this.freshUserLocation();

    //set first entrance flag,if it doesn't exist
    if (!StorageUtil.get(Constants.STORAGE_IS_USER_FIRST_USER_STAT)) {
      StorageUtil.set(Constants.STORAGE_IS_USER_FIRST_USER_STAT, "false");
    }
  }
}
</script>

<style>
.el-message-box {
  box-sizing: border-box;
}
</style>

<style scoped>
.map {
  width: 100vw;
  height: 100vh;
  color: #d7dbdf;

}
.cuisine-select-wrapper {
  position: absolute;
  top: 5%;
  right: 5%;
}
</style>