<template>
  <div class="wrapper">
    <button
        style="border: none;background: transparent;"
        @click="this.openState = true"
    >
      <el-image
          src="/favorite.png"
      />
    </button>
    <el-drawer
        :size="ClientVersionUtil.isMobile()?'85%':'30%'"
        v-model="openState"
        title="Favorite"
        direction="ltr"
    >
      <div>
        <ul
            style="padding: 0;margin-top: 0;"
        >
          <nut-swipe
              v-for="item in items"
          >
            <li
                style="list-style: none;margin-top: 1px;margin-left:4px;"
            >
              <CuisineListItem
                  :info="item"
              />
            </li>
            <template #right>
              <nut-button shape="square" style="height:100%" type="danger">删除</nut-button>
            </template>
          </nut-swipe>
        </ul>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import CuisineListItem from "@/components/CuisineListItem.vue";
import ClientVersionUtil from "@/utils/ClientVersionUtil";

export default {
  name: "FavoriteCuisine",
  computed: {
    ClientVersionUtil() {
      return ClientVersionUtil
    }
  },
  components: {CuisineListItem},
  methods: {},
  data() {
    return {
      openState: false,
      items: [
        {
          "types": [
            "bar",
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/bar-71.png",
          "rating": 4.3,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jm90iR6PnywIuE8fY5TGLqim_V-VqgXA7bP2Nckudslbqb1xzWLGP0VpPaQlxNnXzX-Xm9NLPp69MJUnZXFji9c6m_JG0TdzxzFIl6x_uvJrbir2T15WMMgCVE3ViCNVKXMTzX704BI2bAxULk6UyhyWQgwheL7Ma6eXXYsda0xrvVD",
              "width": 1280,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/103343137169413562216\">A Google User</a>"
              ],
              "height": 1280
            }
          ],
          "reference": "ChIJL6V-u8twfkgRWycbo-lRkFs",
          "user_ratings_total": 1184,
          "price_level": 1,
          "scope": "GOOGLE",
          "name": "Three Bulls Heads",
          "opening_hours": {
            "open_now": true
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.618468879892722,
                "lat": 54.97444367010728
              },
              "northeast": {
                "lng": -1.615769220107278,
                "lat": 54.97714332989272
              }
            },
            "location": {
              "lng": -1.6169307,
              "lat": 54.9756469
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/bar_pinlet",
          "vicinity": "57 Percy St, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X9GM+76 Newcastle upon Tyne",
            "global_code": "9C6WX9GM+76"
          },
          "place_id": "ChIJL6V-u8twfkgRWycbo-lRkFs"
        },
        {
          "types": [
            "bar",
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/bar-71.png",
          "rating": 4.3,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jlYudQAJOeemd2DL-0E7Ido0g17spC23HO8S7DDr9zQErjI6aTrE3xDagg0eG2wq0IbsadFe8usWiK95TqtK1FuSv9rfrPsUTes94_6oqnvKGFxUtiAzkOYbz5-mI2HOJQGMprOVcu6XtdIoN4tjpHGrZGeBOg6ZzMNKZKy3yl_A41x",
              "width": 5056,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/105154396535576653255\">Stephen Foster</a>"
              ],
              "height": 3792
            }
          ],
          "reference": "ChIJHcBxFrZwfkgREAsCeaGNLds",
          "user_ratings_total": 953,
          "price_level": 2,
          "scope": "GOOGLE",
          "name": "Barluga",
          "opening_hours": {
            "open_now": true
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.612954879892722,
                "lat": 54.97020252010727
              },
              "northeast": {
                "lng": -1.610255220107278,
                "lat": 54.97290217989271
              }
            },
            "location": {
              "lng": -1.6116628,
              "lat": 54.9715198
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/bar_pinlet",
          "vicinity": "35 Grey St, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X9CQ+J8 Newcastle upon Tyne",
            "global_code": "9C6WX9CQ+J8"
          },
          "place_id": "ChIJHcBxFrZwfkgREAsCeaGNLds"
        },
        {
          "types": [
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
          "rating": 4.2,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jmfWtyfkSLO5yM0k7LDHCgt4DycIXyJpYVsfgAtpx624Lh3l_QnkmzFmu2JGfsYU5KgPt8zgukk6L51sJyZcMeJeQbkKJ7AucWCi6DV7gnS7bCxUOdcmnDktCmu3XGe5_eJgXgStqRCqbY_fTVdzbYYRr6p23auGl8wQEw_AfFljVvr",
              "width": 3120,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/104573472697678452998\">A Google User</a>"
              ],
              "height": 4160
            }
          ],
          "reference": "ChIJuwwzPcpwfkgRdVW199bCcDc",
          "user_ratings_total": 651,
          "price_level": 2,
          "scope": "GOOGLE",
          "name": "George's Fish Shack, Newcastle",
          "opening_hours": {
            "open_now": false
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.616657279892722,
                "lat": 54.97279007010727
              },
              "northeast": {
                "lng": -1.613957620107278,
                "lat": 54.97548972989271
              }
            },
            "location": {
              "lng": -1.615312,
              "lat": 54.974109
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
          "vicinity": "George's Fish Shack, Greys Quarter, Eldon Square, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X9FM+JV Newcastle upon Tyne",
            "global_code": "9C6WX9FM+JV"
          },
          "place_id": "ChIJuwwzPcpwfkgRdVW199bCcDc"
        },
        {
          "types": [
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
          "rating": 4.6,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jnTMA20zWG6Zr44U2tBURvnfZaLehUAJSCa4pH6UGCUPY08bSJthvtVjcCo_KwjMvJxmyCnp_SW2vCnndH7wS9LK63zrQl1G6vGaDqnAyBsOB6VlXFq3mBb-UH5COOtowaMTXJFsqQBFtBskhTJUhr_lkMCfpmikTZpHke6K5gSKbnh",
              "width": 2828,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/108997209684120075797\">Lam Kitman</a>"
              ],
              "height": 4000
            }
          ],
          "reference": "ChIJqzpqTrVwfkgRt-kUaJlx2_8",
          "user_ratings_total": 1445,
          "price_level": 2,
          "scope": "GOOGLE",
          "name": "Blackfriars Restaurant",
          "opening_hours": {
            "open_now": false
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.620897279892722,
                "lat": 54.97027412010727
              },
              "northeast": {
                "lng": -1.618197620107278,
                "lat": 54.97297377989272
              }
            },
            "location": {
              "lng": -1.6196011,
              "lat": 54.9716724
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
          "vicinity": "Friars St, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X9CJ+M5 Newcastle upon Tyne",
            "global_code": "9C6WX9CJ+M5"
          },
          "place_id": "ChIJqzpqTrVwfkgRt-kUaJlx2_8"
        },
        {
          "types": [
            "bar",
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/bar-71.png",
          "rating": 4.3,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jmmbxjMsvki9dTs0h7zBbEGfTU7SAVB8F7Nn_9OHjydanyUNijVHgsuo6JMEoLo99VtoTDGYyVg-60rIwqP9bL7A2sE9bFf8TUsVTqMvIzKdJ9HJcj1rXDK8w6ImaVoh-b8gjpcwrzB-gcQVR9esESKvo7HIklvvjB7k2u2XDpxqYwb",
              "width": 2048,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/117814132095517918429\">A Google User</a>"
              ],
              "height": 1536
            }
          ],
          "reference": "ChIJt3twDrZwfkgRFc12mpeQRVE",
          "user_ratings_total": 2164,
          "price_level": 2,
          "scope": "GOOGLE",
          "name": "Old George Inn",
          "opening_hours": {
            "open_now": true
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.614210879892722,
                "lat": 54.96984587010729
              },
              "northeast": {
                "lng": -1.611511220107278,
                "lat": 54.97254552989273
              }
            },
            "location": {
              "lng": -1.6127414,
              "lat": 54.9713287
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/bar_pinlet",
          "vicinity": "Old George Yard, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X9CP+GW Newcastle upon Tyne",
            "global_code": "9C6WX9CP+GW"
          },
          "place_id": "ChIJt3twDrZwfkgRFc12mpeQRVE"
        },
        {
          "types": [
            "bar",
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
          "rating": 4.6,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jkj5eGBEEao1OzKH1wlG78Z-SCTaRXWY4qxMtacCodQHEpC23c5RFTFrekuugkQg4hmRJDEKaKH67hi6bvs_eJsKl6ggWWhSv19kfG7M4p4BEGwiAE16VnmsN1lpWxUdvht3QI3vK7_moApbPewDCztFS2Hur_41AGFp_yDXwQLdc8J",
              "width": 3688,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/116568933957272211263\">Stuart Laws</a>"
              ],
              "height": 2075
            }
          ],
          "reference": "ChIJ3Sm-m8pwfkgR8vHJpdzpxek",
          "user_ratings_total": 2469,
          "price_level": 2,
          "scope": "GOOGLE",
          "name": "Turtle Bay Newcastle",
          "opening_hours": {
            "open_now": true
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.618994279892722,
                "lat": 54.97138507010727
              },
              "northeast": {
                "lng": -1.616294620107278,
                "lat": 54.97408472989272
              }
            },
            "location": {
              "lng": -1.617735,
              "lat": 54.9727166
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
          "vicinity": "117 Newgate St, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X9FJ+3W Newcastle upon Tyne",
            "global_code": "9C6WX9FJ+3W"
          },
          "place_id": "ChIJ3Sm-m8pwfkgR8vHJpdzpxek"
        },
        {
          "types": [
            "bar",
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/bar-71.png",
          "rating": 4.4,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jmO64X6NyMMEyTCPuFH3_O3LsmhgJWlO_NbCy6jsujiZNuJndueAzlFDqhBSxq9zlJBS91nqwv7NaR5ns_PgnNiP4Ge2zOno8ylTVr35P5rSsQzSPn-yMVzQA0S0ghuHRc-HyxKVlM5Q-KGJyxHO7JZrKFVnZRperE6UZ1n_gP57rC8",
              "width": 3024,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/109689115912508983800\">Anne Winterhalter</a>"
              ],
              "height": 4032
            }
          ],
          "reference": "ChIJpwMaMC53fkgRcbCVeOHvyzg",
          "user_ratings_total": 459,
          "price_level": 2,
          "scope": "GOOGLE",
          "name": "Cosy Dove",
          "opening_hours": {
            "open_now": true
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.632135179892722,
                "lat": 54.98232482010728
              },
              "northeast": {
                "lng": -1.629435520107278,
                "lat": 54.98502447989272
              }
            },
            "location": {
              "lng": -1.630808,
              "lat": 54.98369400000001
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/bar_pinlet",
          "vicinity": "Hunter's Rd, Spital Tongues, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X9M9+FM Newcastle upon Tyne",
            "global_code": "9C6WX9M9+FM"
          },
          "place_id": "ChIJpwMaMC53fkgRcbCVeOHvyzg"
        },
        {
          "types": [
            "restaurant",
            "bar",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
          "rating": 4.3,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jlSL10eVfpOy8QhoE9yvILVP6AOB5hzOOAyMf0rO9gM5z5SgY21BMIpiiQKxa77XUYsvKKBbinMfzPq7FP0SHXddXtNgZk84qEl-2ykewRXtWuuIz7_pbel2ZWXaCph_Hl9YygAk7UCXKPo2-KxEOlwR-qEcayVyZ9dh9v1Hup6I1Yl",
              "width": 4624,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/102398358986883309958\">Cath Simpkins</a>"
              ],
              "height": 3468
            }
          ],
          "reference": "ChIJT-I3BbZwfkgRzUF9wMAGSLs",
          "user_ratings_total": 2107,
          "price_level": 2,
          "scope": "GOOGLE",
          "name": "Pleased To Meet You",
          "opening_hours": {
            "open_now": true
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.613869979892722,
                "lat": 54.97064152010728
              },
              "northeast": {
                "lng": -1.611170320107278,
                "lat": 54.97334117989272
              }
            },
            "location": {
              "lng": -1.612508,
              "lat": 54.97205049999999
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
          "vicinity": "41-45 High Bridge, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X9CP+RX Newcastle upon Tyne",
            "global_code": "9C6WX9CP+RX"
          },
          "place_id": "ChIJT-I3BbZwfkgRzUF9wMAGSLs"
        },
        {
          "types": [
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
          "rating": 3.8,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jnhnw2ChBHp4euRs0N2t7JMCxCwiPrMWaVFSHK4R2kyP3YLPH8nJaFpMBnrW9cWV1Mt9WwH1ZTXGim5aYDNEx9Eu9BcUShMZ_6OROVO-1gZL_2-82Qs08vBdsC2CUG1jwosZxux1lzFT1DHSR14H7iQ-qbRtlCibC-0oTz5vQgkb4km",
              "width": 4160,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/109880376876567259077\">Rachel Baron</a>"
              ],
              "height": 2340
            }
          ],
          "reference": "ChIJh8_NYrNwfkgRY9ixUKkXoq4",
          "user_ratings_total": 51,
          "scope": "GOOGLE",
          "name": "Hawthorns Brasserie",
          "opening_hours": {
            "open_now": false
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.618931579892722,
                "lat": 54.96574637010727
              },
              "northeast": {
                "lng": -1.616231920107278,
                "lat": 54.96844602989271
              }
            },
            "location": {
              "lng": -1.618073,
              "lat": 54.9669451
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
          "vicinity": "Hawthorn House, Hawthorn Sq, Forth St, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X98J+QQ Newcastle upon Tyne",
            "global_code": "9C6WX98J+QQ"
          },
          "place_id": "ChIJh8_NYrNwfkgRY9ixUKkXoq4"
        },
        {
          "types": [
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
          "rating": 4.4,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jnwY6bDF8HO1IxvAkCHVeuNtt7ACCEBDJyaVJbD1hUQdhNCQiq4VjmKzU7gPrUFVlxSUppAe5LFYRugcnIDNK6G7ZmDNpX9lSoaAr7ZQtUdI4W5uL8twuifBgZF9L1aADsNxjFrh6pSYWl10vBgP5CB8PDC_ncxHoo8dRqrZn4TIpu7",
              "width": 4032,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/112391451675086094268\">John Edwards</a>"
              ],
              "height": 2268
            }
          ],
          "reference": "ChIJxfjlLCxxfkgRPDb8tANM6Xo",
          "user_ratings_total": 334,
          "scope": "GOOGLE",
          "name": "Chart House",
          "opening_hours": {
            "open_now": true
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.605668429892722,
                "lat": 54.96790407010728
              },
              "northeast": {
                "lng": -1.602968770107278,
                "lat": 54.97060372989272
              }
            },
            "location": {
              "lng": -1.6043207,
              "lat": 54.9692618
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
          "vicinity": "63 Quayside, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X99W+P6 Newcastle upon Tyne",
            "global_code": "9C6WX99W+P6"
          },
          "place_id": "ChIJxfjlLCxxfkgRPDb8tANM6Xo"
        },
        {
          "types": [
            "bar",
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/bar-71.png",
          "rating": 4.2,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jlK4pcurK4qt-mJONnOfNcxLg3gIH5efRFtlrLkHF9yeyBxu7sPrxckvlsDmKj1DKLjCh5z3oQvhhl9WfXGDsSpUPJPhFiXYH3TPdL8nUF5r0FGQct6WcqFvAa7_C-ORJTEO67zVG7EKNwWG7B2pfMwGHMqz-8eA8o7TtgvOYJ0gSG4",
              "width": 4000,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/102193866126929684305\">Sterne Neu Ordnen</a>"
              ],
              "height": 3000
            }
          ],
          "reference": "ChIJs9lwQclwfkgRuNOsEZxdQqw",
          "user_ratings_total": 1705,
          "price_level": 2,
          "scope": "GOOGLE",
          "name": "City Tavern",
          "opening_hours": {
            "open_now": true
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.613777829892722,
                "lat": 54.97543647010728
              },
              "northeast": {
                "lng": -1.611078170107278,
                "lat": 54.97813612989273
              }
            },
            "location": {
              "lng": -1.6124558,
              "lat": 54.9767854
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/bar_pinlet",
          "vicinity": "10 Northumberland Rd, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X9GQ+P2 Newcastle upon Tyne",
            "global_code": "9C6WX9GQ+P2"
          },
          "place_id": "ChIJs9lwQclwfkgRuNOsEZxdQqw"
        },
        {
          "types": [
            "restaurant",
            "bar",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
          "rating": 4.4,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jkFiOIvjiNrvYB4szL3UcYXWrzElZ6vhuNjJXi3n3qmzFiMVLFrhboqMmViEYpE2fwfivkvEf24mA5yB-uszlna1gzYDuMpL5KsYFPwJ-zqx9zTT28QQ7nBsoy3HM5d0h10l5jLT5ZlLUmtnd3xF1VxIfztjzjpkX2ClY8hanvWaY-A",
              "width": 4080,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/105155960127186724830\">harry kotakidis</a>"
              ],
              "height": 3072
            }
          ],
          "reference": "ChIJf1GnjslwfkgRkfxHAcyX_AE",
          "user_ratings_total": 4696,
          "price_level": 2,
          "scope": "GOOGLE",
          "name": "The Botanist Bar & Restaurant Newcastle",
          "opening_hours": {
            "open_now": true
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.614756779892722,
                "lat": 54.97277037010728
              },
              "northeast": {
                "lng": -1.612057120107278,
                "lat": 54.97547002989272
              }
            },
            "location": {
              "lng": -1.6133963,
              "lat": 54.9742584
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
          "vicinity": "Monument Mall, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X9FP+PJ Newcastle upon Tyne",
            "global_code": "9C6WX9FP+PJ"
          },
          "place_id": "ChIJf1GnjslwfkgRkfxHAcyX_AE"
        },
        {
          "types": [
            "bar",
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
          "rating": 4,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jn-THQdtIRLSXWnemhbQmCpQJFHLYV139CorQvxherc1m-7uOwUHAUpeUCwWLwQ3NnlOtJ_PBBQXO59aGN3Igyy0iQEjmjurL_5cglfc2mr8EYR8DR67NA5E42dNzYo_U76gBBx4ji7Hmouv5VSOk4JmDWtTTDTdP2wEjJTM7yKxIen",
              "width": 1600,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/113796543010012723609\">A Google User</a>"
              ],
              "height": 1200
            }
          ],
          "reference": "ChIJJX-VTLZwfkgRKf5AeYX5Uao",
          "user_ratings_total": 822,
          "price_level": 2,
          "scope": "GOOGLE",
          "name": "Bijoux Burger",
          "opening_hours": {
            "open_now": true
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.611312679892722,
                "lat": 54.96964857010728
              },
              "northeast": {
                "lng": -1.608613020107278,
                "lat": 54.97234822989272
              }
            },
            "location": {
              "lng": -1.6099027,
              "lat": 54.9709417
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
          "vicinity": "10 Mosley St, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X9CR+92 Newcastle upon Tyne",
            "global_code": "9C6WX9CR+92"
          },
          "place_id": "ChIJJX-VTLZwfkgRKf5AeYX5Uao"
        },
        {
          "types": [
            "bar",
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/bar-71.png",
          "rating": 4.5,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jm7-psLrZX7SomIPXtsV3MbH00998obqZK94jaG-6ifFx-1NHaxpgnT_4o9mW0F5c3N8zUHBLPs5eRy3ktBxv94eMfF1E_DspEuf8jTxzTNweDrh0Qq98IQ4YO0cezXbqg1hzxD4uIEsZ4d8fDiXt9BkEUOiupfCgsGf9xjSyu6__aG",
              "width": 3612,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/105826960462382209006\">A Google User</a>"
              ],
              "height": 3024
            }
          ],
          "reference": "ChIJzdu2bLdwfkgRdX0co9yOV6g",
          "user_ratings_total": 1542,
          "price_level": 2,
          "scope": "GOOGLE",
          "name": "Redhouse",
          "opening_hours": {
            "open_now": true
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.609336379892722,
                "lat": 54.96738122010728
              },
              "northeast": {
                "lng": -1.606636720107278,
                "lat": 54.97008087989273
              }
            },
            "location": {
              "lng": -1.6080531,
              "lat": 54.9687673
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/bar_pinlet",
          "vicinity": "32 Sandhill, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X99R+GQ Newcastle upon Tyne",
            "global_code": "9C6WX99R+GQ"
          },
          "place_id": "ChIJzdu2bLdwfkgRdX0co9yOV6g"
        },
        {
          "types": [
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
          "rating": 4.7,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jkJJwVDnpicrF9Wmk3Cl9aUEBBCV8HDfKA9BGgEeyXbNSR3TZNBxN4BImlCx6HC40ba_th7vw12updebe4psGcKmIme0pcd3MQGDnDK_3YgcgYdGUxDrP4mekV0mkpk2O8CtHaIwftpulEZkFY2VkR_5THUNu5-TF-ME0JAp1UqxbMy",
              "width": 5048,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/117413341817676046408\">A Google User</a>"
              ],
              "height": 3365
            }
          ],
          "reference": "ChIJIUdV2rZwfkgR5-uvmFBQqq4",
          "user_ratings_total": 475,
          "price_level": 4,
          "scope": "GOOGLE",
          "name": "House of Tides",
          "opening_hours": {
            "open_now": false
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.611556679892722,
                "lat": 54.96658457010728
              },
              "northeast": {
                "lng": -1.608857020107278,
                "lat": 54.96928422989272
              }
            },
            "location": {
              "lng": -1.6102153,
              "lat": 54.96792989999999
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
          "vicinity": "28-30 Close, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X99Q+5V Newcastle upon Tyne",
            "global_code": "9C6WX99Q+5V"
          },
          "place_id": "ChIJIUdV2rZwfkgR5-uvmFBQqq4"
        },
        {
          "types": [
            "bar",
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/bar-71.png",
          "rating": 4.6,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jnT8vqbm687yxsJwsR3zfidjlF6ofUbHIeBn1wRg08haND42ibqTKs_kYcV-XOddZUiM12xWUxVjusJlfLsbW9PSM5Fn_SPjkCsCIMPVAXvkCmcycKYu00xJ-dCU1ZgEKq7YYq97ZLdWKkBjriM1v23v6zn0p7M3OSHC-Iyg0HGKvzC",
              "width": 4624,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/108210864180014688801\">Julie Hamlyn</a>"
              ],
              "height": 2604
            }
          ],
          "reference": "ChIJ-4RcsblwfkgRFDoYJIdu51I",
          "user_ratings_total": 1173,
          "price_level": 2,
          "scope": "GOOGLE",
          "name": "The Broad Chare",
          "opening_hours": {
            "open_now": false
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.605888929892722,
                "lat": 54.96851942010727
              },
              "northeast": {
                "lng": -1.603189270107278,
                "lat": 54.97121907989272
              }
            },
            "location": {
              "lng": -1.6046085,
              "lat": 54.96984990000001
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/bar_pinlet",
          "vicinity": "25 Broad Chare, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X99W+W5 Newcastle upon Tyne",
            "global_code": "9C6WX99W+W5"
          },
          "place_id": "ChIJ-4RcsblwfkgRFDoYJIdu51I"
        },
        {
          "types": [
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
          "rating": 4.3,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jm8ArChkKYsjPsZaQ8Mmvs5EKEm9SjXtKHuulcVY0jPuaazx4rbqGHKl7rHG-mYTPhOSdhOJmu1ir3AzGQFgLor6iCE06LA5Wwn-EgacZGP6fk3WjpJ74I5nlnjkQP4JO4GlW5H-H5rvKjvS1zh_BrbNa-COPKGE391UYsfVKn07pO5",
              "width": 5416,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/103100099629668823456\">A Google User</a>"
              ],
              "height": 3611
            }
          ],
          "reference": "ChIJh4ugdQlxfkgRgU1mAbOwORU",
          "user_ratings_total": 1207,
          "scope": "GOOGLE",
          "name": "Hard Rock Cafe Newcastle",
          "opening_hours": {
            "open_now": true
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.609102829892722,
                "lat": 54.96706082010728
              },
              "northeast": {
                "lng": -1.606403170107278,
                "lat": 54.96976047989272
              }
            },
            "location": {
              "lng": -1.6078171,
              "lat": 54.9684757
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
          "vicinity": "The Guildhall, Sandhill, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X99R+9V Newcastle upon Tyne",
            "global_code": "9C6WX99R+9V"
          },
          "place_id": "ChIJh4ugdQlxfkgRgU1mAbOwORU"
        },
        {
          "types": [
            "bar",
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
          "rating": 4.7,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jnE2a4NT9aoMW4Wn0CxgYrRNcglMd8w_3fzo_-J5_AU4AZTzo6m8gcdC8AZ5ztVaJ9rdQa-BFW5ERbZt7222sJ-qu3AQzPp2qDD3PTl3kXZ13pZFtNB-DaQK0RupLPv4zG1raEYr8DA0g1kYG80aJ_UL7waug-BDmDO4jGF3tYtyx1b",
              "width": 5760,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/107671960030090562518\">A Google User</a>"
              ],
              "height": 3840
            }
          ],
          "reference": "ChIJhRm5fLdwfkgRXfscVsbAcq8",
          "user_ratings_total": 570,
          "price_level": 3,
          "scope": "GOOGLE",
          "name": "21",
          "opening_hours": {
            "open_now": false
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.605994079892722,
                "lat": 54.96922142010727
              },
              "northeast": {
                "lng": -1.603294420107278,
                "lat": 54.97192107989272
              }
            },
            "location": {
              "lng": -1.6046369,
              "lat": 54.97059609999999
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
          "vicinity": "One Trinity Gardens, Pandon, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X9CW+64 Newcastle upon Tyne",
            "global_code": "9C6WX9CW+64"
          },
          "place_id": "ChIJhRm5fLdwfkgRXfscVsbAcq8"
        },
        {
          "types": [
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
          ],
          "business_status": "OPERATIONAL",
          "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
          "rating": 4.2,
          "icon_background_color": "#FF9E67",
          "photos": [
            {
              "photo_reference": "AUjq9jkEQUVIppjnPvDSwMnw9-AIuCygz_0EnEgXN5dE_-AaKoXh1lyTQp8r4UNoudiBFUmCb6GMBUmAj3Nqol_a_8DwcjvIURutN8uNuOLaIwenGOtHzT9lCq3TEXuLaFZeflTwQau_Y9yzzUXYTLulhLn2HiQgNHfNYPbXcUJYzbta3TM2",
              "width": 1728,
              "html_attributions": [
                "<a href=\"https://maps.google.com/maps/contrib/114452440215297145112\">Silk Room Restaurant and Champagne Bar</a>"
              ],
              "height": 1152
            }
          ],
          "reference": "ChIJN1VfNrhwfkgRVQ458E4KL_o",
          "user_ratings_total": 190,
          "price_level": 3,
          "scope": "GOOGLE",
          "name": "Silk Room Restaurant & Champagne Room",
          "opening_hours": {
            "open_now": false
          },
          "geometry": {
            "viewport": {
              "southwest": {
                "lng": -1.606023679892722,
                "lat": 54.96925562010728
              },
              "northeast": {
                "lng": -1.603324020107278,
                "lat": 54.97195527989273
              }
            },
            "location": {
              "lng": -1.6047412,
              "lat": 54.9706645
            }
          },
          "icon_mask_base_uri": "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
          "vicinity": "Pandon, Trinity Gardens, Newcastle upon Tyne",
          "plus_code": {
            "compound_code": "X9CW+74 Newcastle upon Tyne",
            "global_code": "9C6WX9CW+74"
          },
          "place_id": "ChIJN1VfNrhwfkgRVQ458E4KL_o"
        }
      ],

    }
  }
}
</script>

<style scoped>
.wrapper:deep(.el-drawer__body) {
  padding: 0;
  overflow-x: hidden;
}
</style>