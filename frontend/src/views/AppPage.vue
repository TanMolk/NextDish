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
      :center="userLocation"
      :zoom="15"
      :disable-default-ui="true"
  >
    <CustomMarker
        :options="userMarkerOption"
    >
      <img style="opacity: 0.7;width: 2em;height: 2em" src="@/assets/me.png" alt="me"/>
    </CustomMarker>
    <Circle
        :options="userCircleOption"
    />

    <Marker
        v-for="item in restaurantData.items"
        :options="{
          label: item.name,
          position: item.geometry.location,
          title: item.rating + '',
        }"
        @click="markerClick(item)"
    />
  </GoogleMap>
  <CuisineTypeSelect
      class="cuisine-select-wrapper"
      @option-change="selectOptionChange"
  />
  <CuisineList
      ref="cuisineList"
      v-show="listShowState"
      :map-instance="mapInstance"
      :items="restaurantData.items"
      :place-id="restaurantData.detailPlaceId"
      class="app-cuisine-list"
      @click-X="this.listShowState = false"
      @place-id-change="onCuisineListChangePlaceId($event)"
      @direction-request="onDirectionRequest($event)"
  />
</template>

<script>
import {GoogleMap, Marker, CustomMarker, Circle} from 'vue3-google-map'
import {mileToMeter} from "@/utils/MileUtil";
import CuisineTypeSelect from "@/components/CuisineTypeSelect.vue";
import CuisineList from "@/components/CuisineList.vue";
import Constants from "@/constants/Constants";
import GoogleMapPlaceService from "@/service/GoogleMapPlaceService";
import {ElMessageBox} from 'element-plus';
import StorageUtil from "@/utils/StorageUtil";

export default {
  name: "AppPage",
  computed: {
    Constants() {
      return Constants
    }
  },
  components: {CuisineList, CuisineTypeSelect, GoogleMap, Marker, CustomMarker, Circle},
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
        //items of all restaurants
        items: [],
        //data of the detail windows
        detailPlaceId: null
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
      listShowState: true,
    }
  },
  methods: {
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
        //Set the callback function
        navigator.geolocation.getCurrentPosition((position) => {
          //If success
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
            //close loading page
            this.$loading({
              fullscreen: true
            }).close();
          }
          this.freshUserMarkerOptions();

          //store location for further usage
          StorageUtil.set("currentPositionLat", this.userLocation.lat)
          StorageUtil.set("currentPositionLng", this.userLocation.lng)

          //If error
        }, (error) => {
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
        });
      }
    },
    /**
     * Control event when selection get change
     * @param type restaurant type
     */
    selectOptionChange(type) {
      this.loadRecentRestaurant(type);
      this.listShowState = true;
      //call sub component method
      this.$refs.cuisineList.changeInfoWindow("list");
    },
    /**
     * Refresh nearby restaurant data set.
     * @param type Type of restaurant, like America, British
     */
    async loadRecentRestaurant(type) {
      let response = await GoogleMapPlaceService.getRestaurantWithKeywordInOneMile(this.userLocation, type);
      this.restaurantData.items = response.data.results;
    },
    /**
     * What happens, if one restaurant marker get clicked
     * @param item Data of the marker
     */
    markerClick(item) {
      this.listShowState = true;
      this.mapInstance.map.panTo(item.geometry.location);
      this.restaurantData.detailPlaceId = item.place_id;
      this.$refs.cuisineList.changeInfoWindow("detail", item);
    },
    /**
     * when place-id-change event happens, the script runs.
     * @param data place_id
     */
    onCuisineListChangePlaceId(data) {
      this.restaurantData.detailPlaceId = data;
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
  }
}
</script>

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

.app-cuisine-list {
  /*max-height: 50%;*/
}
</style>