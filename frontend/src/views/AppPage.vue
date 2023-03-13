<template>
  <GoogleMap
      ref="mapInstance"
      class="map"
      :map-id="Constants.GOOGLE_MAP_ID"
      :api-key="Constants.GOOGLE_MAP_API_KEY"
      :center="getLocation()"
      :zoom="17"
      :disable-default-ui="true"
  >
    <CustomMarker
        :options="{ position: getLocation()}"
    >
      <img style="opacity: 0.7;width: 2em;height: 2em" src="@/assets/me.png" alt="me"/>
    </CustomMarker>
    <Circle
        :options="{center: getLocation(), radius: convertMileToMeter(1),strokeColor:'#d7dbdf'}"
    />

    <Marker
        v-for="item in restaurantData.items"
        :options="{
          label: item.name,
          position: item.geometry.location,
          title: item.rating + '',
        }"
        @click="marketClick"
    />
  </GoogleMap>
  <CuisineTypeSelect
      class="cuisine-select-wrapper"
      @option-change="selectOptionChange"
  />
  <CuisineList
      v-show="listShowState"
      :items="restaurantData.items"
      :detail="restaurantData.detail"
      class="app-cuisine-list"
      @click-X="this.listShowState = false"
  />
</template>

<script>
import {GoogleMap, Marker, CustomMarker, Circle} from 'vue3-google-map'
import {mileToMeter} from "@/utils/MileUtil";
import CuisineTypeSelect from "@/components/CuisineTypeSelect.vue";
import CuisineList from "@/components/CuisineList.vue";
import {reactive} from "vue";
import Constants from "@/constants/Constants";

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
      mapInstance: null,
      myLocation: {
        latitude: 55,
        longitude: -1.6,
      },
      listShowState: true,
      restaurantData: reactive({
        items: [],
        detail: null
      })
    }
  },
  methods: {
    getLocation() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((position) => {
          this.myLocation.latitude = position.coords.latitude;
          this.myLocation.longitude = position.coords.longitude;
        });
        return {lat: this.myLocation.latitude, lng: this.myLocation.longitude}
      }
    },
    convertMileToMeter(miles) {
      return mileToMeter(miles)
    },
    selectOptionChange(type) {
      console.log(this.mapInstance)
      let pyrmont = new this.mapInstance.api.LatLng(this.myLocation.latitude, this.myLocation.longitude);

      let placesService = new this.mapInstance.api.places.PlacesService(this.mapInstance.map);

      let request = {
        location: pyrmont,
        radius: this.convertMileToMeter(1),
        type: ['restaurant'],
        keyword: type
      };

      placesService.nearbySearch(request, (resp) => {
        this.restaurantData.items = reactive([])
        resp.forEach(d => this.restaurantData.items.push(d))
        console.log(resp)
        console.log(this.restaurantData.items)
      });

      console.log(placesService);
    },
    loadRecentRestaurant() {
    },
    marketClick() {
      this.listShowState = true
    }
  },
  created() {
    this.getLocation();
  },
  beforeUpdate() {
    this.mapInstance = this.$refs.mapInstance;
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