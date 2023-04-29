<template>
  <teleport to="body">
    <el-dialog
        style="border-radius: 8px;"
        v-model="openState"
        :width="ClientVersionUtil.isMobile()? '98%' : '50%'"
        :center="true"
        align-center
    >
      <template v-slot:header>
        <p class="title">{{ titles[titleIndex] }}</p>
      </template>

      <div class="input-area">
        <transition
            name="custom-transition"
            enter-active-class="animate__animated animate__fadeInRightBig"
            leave-active-class="animate__animated animate__fadeOut"
        >
          <div v-show="titleIndex === 0">
            <p class="input-title">Email</p>
            <el-input
                v-model="email"
                size="large"
            />
            <p class="input-title">Password
              <span
                  style="float: right"
                  class="text-button"
                  @click="titleIndex = 2"
              >
                Forgot?
              </span>
            </p>
            <el-input
                v-model="password"
                size="large"
                show-password
            />
            <el-button
                ref="subBtn"
                class="submit"
                @click="submit"
            >
              {{ buttonTitles[0] }}
            </el-button>
          </div>
        </transition>

        <transition
            name="custom-transition"
            enter-active-class="animate__animated animate__fadeInRightBig"
            leave-active-class="animate__animated animate__fadeOut"
        >
          <div v-show="titleIndex !== 0">
            <transition
                name="custom-transition"
                enter-active-class="animate__animated animate__fadeInRightBig"
            >
              <div
                  style="margin-bottom: 2em"
              >
                <el-steps
                    :active="currentStep"
                    align-center
                    finish-status="success"
                >
                  <el-step title="Email"/>
                  <el-step title="Verify"/>
                  <el-step title="Password"/>
                </el-steps>
              </div>
            </transition>

            <p
                class="input-title"
                v-show="currentStep !== 1"
            >
              {{ currentStep === 2 ? email : "Email address" }}
            </p>
            <el-input
                v-if="currentStep === 0"
                v-model="email"
                size="large"
            />
            <div>
              <nut-short-password
                  v-model="verifyCode"
                  v-model:visible="verifyCodeInputShow"
                  title="Verify Code"
                  desc="Please enter the code received"
                  :tips="verifyCodeMsg"
                  @tips="resend"
              >
              </nut-short-password>
              <nut-number-keyboard
                  v-model:visible="verifyCodeInputShow"
                  @input="verifyInput"
                  @delete="verifyDelete"
                  @close="this.verifyCodeInputShow = false;"
              />
            </div>
            <transition
                name="custom-transition"
                enter-active-class="animate__animated animate__fadeIn"
            >
              <div
                  v-if="currentStep === 2"
              >
                <p class="input-title">Password</p>
                <el-input
                    v-model="password"
                    size="large"
                    show-password
                />
              </div>
            </transition>
            <el-button
                :loading="verifyCode.length >= 6 && !verifyCodeInputShow"
                ref="subWithStepBtn"
                class="submit"
                @click="submitWithStep"
            >
              {{
                currentStep === 0 ? "Send verify link" : currentStep === 1 ? "Secure Verify" : buttonTitles[titleIndex]
              }}
            </el-button>
          </div>
        </transition>
      </div>

      <template v-slot:footer>
        <div v-if="titleIndex === 0">
          <p class="footer">
            Don't have an account?
            <span
                class="text-button"
                @click="titleIndex = 1;"
            >
            Sign up
          </span>
          </p>
        </div>
        <div v-else-if="titleIndex === 1">
          <p class="footer">
            Already Have an Account?
            <span
                class="text-button"
                @click="titleIndex = 0"
            >
            Login In
          </span>
          </p>
        </div>
      </template>
    </el-dialog>
  </teleport>
</template>

<script>
import ClientVersionUtil from "@/utils/ClientVersionUtil";

export default {
  name: "UserModal",
  computed: {
    ClientVersionUtil() {
      return ClientVersionUtil
    }
  },
  data() {
    return {
      titles: ["Login to your account", "Create an account", "Reset your password"],
      buttonTitles: ["Login now", "Create Account", "Reset"],
      passwordInputShow: false,
      verifyCodeInputShow: false,
      verifyCodeMsg: "",
      currentStep: 0,
      titleIndex: 0,
      openState: false,
      email: "",
      password: "",
      verifyCode: ""
    }
  },
  watch: {
    titleIndex() {
      this.init();
    }
  },
  methods: {
    submit() {

    },
    submitWithStep() {
      if (this.currentStep === 0) {
        this.verifyCodeInputShow = true;
      } else {
        this.currentStep++;
      }
    },
    verifyInput(number) {
      this.verifyCode += number;
      if (this.verifyCode.length === 6) {
        this.verifyCodeInputShow = false;

        this.verifyCode = "";
        this.currentStep++;
      }
    },
    verifyDelete() {
      let verifyCode = this.verifyCode;
      this.verifyCode = verifyCode.substring(0, verifyCode.length - 1);
    },
    resend() {
      console.log("rrr")
    },
    init() {
      this.currentStep = 0;
      this.email = "";
      this.password = "";
      this.verifyCode = "";
      this.passwordInputShow = true;
    }
  }
}
</script>

<style scoped>
.title {
  text-align: center;
  margin: 0;
  font-size: 1.5em;
  font-weight: 600;
}

.input-area {
  margin: 0 auto;
  width: 80%;
}

.input-area:deep(.el-input__wrapper) {
  border-radius: 8px;
}

.text-button {
  color: #007DFA;
  margin-left: 10px;
}

.footer {
  color: #BEBEBF;
}

.input-title {
  margin-bottom: 6px;
}

.submit {
  height: 3em;
  width: 12em;
  color: white;
  background-color: #007DFA;
  border-radius: 8px;
  margin-left: calc(50% - 6em);
  margin-top: 1.5em;
}
</style>