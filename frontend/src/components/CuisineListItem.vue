<!--
 @Name CuisineListItem
 @Description The list item
 @author Wei Tan
 @createDate 2023/03/12
-->
<template>
  <div class="wrapper">
    <el-image
        class="img"
        v-loading="loading"
        :src="imageSrc"
    />
    <div class="info">
      <span class="info-text">
        {{ info.name }}
      </span>
      <span>
        {{ info.types[0] }}
      </span>
    </div>
    <div class="mark">
      🌟{{ info.rating }}
    </div>
  </div>
</template>

<script>
import PlaceService from "@/service/PlaceService";

export default {
  name: "CuisineListItem",
  props: {
    //item data
    info: Object
  },
  data() {
    return {
      //blob url
      imageSrc: null,
      //the state of image loading
      loading: true
    }
  },
  methods: {
    async loadImage() {

      let photos = this.info.photos;
      if (photos) {
        PlaceService.getPlaceImage(photos[0])
            .then(resp => {
              if (resp) {
                const blob = new window.Blob([resp.data], {type: 'image/jpeg'})
                this.imageSrc = URL.createObjectURL(blob);
                this.loading = false;
              }
            })
            .catch(err => {
              console.log(err)
              this.loading = false;
            });
      } else {
        this.imageSrc = "/image-alt.png";
        this.loading = false;
      }
    }
  },
  mounted() {
    //only without blobSrc, loading image
    if (!this.imageSrc) {
      this.loadImage();
    }
  }
}
</script>

<style scoped>
.wrapper {
  display: grid;
  grid-template-columns: repeat(3, 1fr );
  overflow: hidden;

  padding: 10px;
  border: 1px solid white;
}

.img {
  width: 80px;
  height: 80px;
  margin: auto auto;

  border-radius: 1em;
}

.info {
  margin: auto auto auto 1em;
}

.info-text {
  display: block;

  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  /*20 characters*/
  width: 20ch;

  padding-bottom: 20px;
}

.mark {
  margin: auto auto;
}
</style>