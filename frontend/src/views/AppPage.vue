<template>
  <GoogleMap
      class="map"
      :center="getLocation()"
      :zoom="17"
      :disable-default-ui="true"
      ref=""
  >
    <CustomMarker
        :options="{ position: getLocation()}"
    >
      <img style="opacity: 0.7;width: 2em;height: 2em" src="@/assets/me.png" alt="me"/>
    </CustomMarker>
    <Circle
        :options="{center: getLocation(), radius: convertMileToMeter(1),strokeColor:'#d7dbdf'}"
    />
  </GoogleMap>
  <CuisineTypeSelect class="cuisine-select-wrapper"/>
  <CuisineList
      v-show="listShowState"
      class="app-cuisine-list"
      @click-X="listShowState = !listShowState"
  />
</template>

<script>
import {GoogleMap, CustomMarker, Circle} from 'vue3-google-map'
import {mileToMeter} from "@/utils/MileUtil";
import CuisineTypeSelect from "@/components/CuisineTypeSelect.vue";
import CuisineList from "@/components/CuisineList.vue";
import {getCurrentInstance} from "vue";
import {Dialog} from '@nutui/nutui';

export default {
  name: "AppPage",
  components: {CuisineList, CuisineTypeSelect, GoogleMap, CustomMarker, Circle},
  data() {
    return {
      myLocation: {
        latitude: 55,
        longitude: -1.6,
      },
      listShowState: true
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
    }
  },
  created() {
    this.getLocation();
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