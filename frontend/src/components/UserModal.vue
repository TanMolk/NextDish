<!--
 @Name UserModel
 @Description Component for user login, user registration, password reset
 @author Wei Tan
 @createDate 2023/04/28
-->
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
          <div v-if="titleIndex === 0">
            <el-form
                ref="form"
                :rules="validRuleForm"
                :model="formData"
            >
              <p class="input-title">Email</p>
              <el-form-item prop="email">
                <el-input v-model="formData.email" size="large"/>
              </el-form-item>

              <p class="input-title">Password
                <span
                    style="float: right"
                    class="text-button"
                    @click="titleIndex = 2"
                >
                Forgot?
              </span>
              </p>
              <el-form-item prop="password">
                <el-input
                    v-model="formData.password"
                    size="large"
                    show-password
                />
              </el-form-item>
              <el-button
                  ref="subBtn"
                  class="submit"
                  :loading="buttonLoading"
                  @click="submit"
              >
                {{ buttonTitles[0] }}
              </el-button>
            </el-form>
          </div>
        </transition>

        <transition
            name="custom-transition"
            enter-active-class="animate__animated animate__fadeInRightBig"
            leave-active-class="animate__animated animate__fadeOut"
        >
          <div v-if="titleIndex !== 0">
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

            <el-form
                ref="step0Form"
                :model="formData"
                :rules="validRuleForm"
            >
              <p
                  class="input-title"
                  v-if="currentStep !== 1"
              >
                {{ currentStep === 2 ? formData.email : "Email address" }}
              </p>
              <el-form-item prop="email">
                <el-input
                    v-if="currentStep === 0"
                    v-model="formData.email"
                    size="large"
                />
              </el-form-item>
            </el-form>
            <div>
              <nut-short-password
                  v-model="formData.verifyCode"
                  v-model:visible="verifyCodeInputShow"
                  title="Verify Code"
                  desc="Please enter the code received"
                  :error-msg="verifyCodeMsg"
                  tips="Resend"
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
                <el-form
                    ref="step2Form"
                    :model="formData"
                    :rules="validRuleForm"
                >
                  <el-form-item prop="password">
                    <el-input
                        v-model="formData.password"
                        size="large"
                        show-password
                    />
                  </el-form-item>
                </el-form>
              </div>
            </transition>
            <el-button
                :loading="buttonLoading"
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
import UserService from "@/service/UserService";
import {ElNotification} from "element-plus";
import StorageUtil from "@/utils/StorageUtil";
import Constants from "@/constants/Constants";
import UserData from "@/constants/UserData";

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
      buttonLoading: false,
      currentStep: 0,
      titleIndex: 0,
      openState: false,
      formData: {
        email: "",
        password: "",
        verifyCode: ""
      },
      validRuleForm: {
        email: [
          {
            required: true,
            message: 'Please enter your login email',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: 'Please enter your password',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  watch: {
    titleIndex() {
      this.init();
    }
  },
  methods: {
    /**
     * Funtion of login button
     * @returns {Promise<void>} successful login
     */
    async submit() {
      this.buttonLoading = true;
      await this.$refs.form.validate(async valid => {
        if (valid) {
          let resp = await UserService.login(this.formData.email, this.formData.password);

          if (resp) {
            ElNotification({
              type: 'success',
              message: 'Login success',
            })
            StorageUtil.set(Constants.STORAGE_TOKEN, resp.data.token);
            UserData.freshUserData();
            this.openState = false;
          }
        }
      });
      this.buttonLoading = false;
    },
    /**
     * function of login registration and password reset,  with various condition check, notify user the process is success or fail
     */
    submitWithStep() {
      this.buttonLoading = true;
      //validate user email existent in database
      if (this.currentStep === 0) {
        this.$refs.step0Form.validate(async valid => {
          if (valid) {
            let resp = await UserService.exist(this.formData.email);
            if (!resp.data) {
              if (this.titleIndex === 2) {
                ElNotification({
                  type: 'warning',
                  message: `Account doesn't exist`,
                });
                this.buttonLoading = false;
                return;
              }
            } else {
              if (this.titleIndex === 1) {
                ElNotification({
                  type: 'warning',
                  message: `Account exists`,
                });
                this.buttonLoading = false;
                return;
              }
            }
            //check if the verify code is sent
            UserService.sendVerifyCode(this.formData.email)
                .then((resp) => {
                  if (resp) {
                    this.verifyCodeInputShow = true;
                    this.currentStep++;
                  }
                  this.buttonLoading = false;
                })
                .catch(err => {
                  console.log(err)
                  ElNotification({
                    type: 'warning',
                    message: `Send verify code fail, please try later`,
                  });
                  this.buttonLoading = false;
                });
          }
        });
      } else if (this.currentStep === 1) {
        this.verifyCodeMsg = ""
        this.verifyCodeInputShow = true;
        //check the account is created successfully
      } else if (this.currentStep === 2) {
        this.$refs.step2Form.validate(valid => {
          if (valid) {
            if (this.titleIndex === 1) {
              UserService.signIn(this.formData.email, this.formData.password, this.formData.verifyCode)
                  .then(resp => {
                    if (resp) {
                      if (resp.data) {
                        StorageUtil.set(Constants.STORAGE_TOKEN, resp.data.token);
                        ElNotification({
                          type: 'success',
                          message: 'Create success',
                        });
                        setTimeout(() => {
                          this.openState = false;
                        }, 500)
                      } else {
                        ElNotification({
                          type: 'warning',
                          message: `This email has been occupied, please try another`,
                        });
                      }
                    }
                    this.buttonLoading = false;
                  })
                  .catch(err => {
                    console.log(err)
                    ElNotification({
                      type: 'error',
                      message: 'Code has expired',
                    })
                    this.buttonLoading = false;
                    this.openState = false;
                  });
            } else if (this.titleIndex === 2) {
              //check if the password reset success or fail
              UserService.resetPassword(this.formData.email, this.formData.password, this.formData.verifyCode)
                  .then(resp => {
                    if (resp) {
                      if (resp.data) {
                        ElNotification({
                          type: 'success',
                          message: 'Reset success',
                        });
                        setTimeout(() => {
                          this.titleIndex = 0;
                        }, 500)
                      } else {
                        ElNotification({
                          type: 'warning',
                          message: `Reset fail, please try later`,
                        });
                      }
                    }
                    this.currentStep++
                    this.buttonLoading = false;
                  })
                  .catch(err => {
                    console.log(err)
                    ElNotification({
                      type: 'error',
                      message: 'Code has expired',
                    })
                    this.buttonLoading = false;
                    this.openState = false;
                  })
            }
          }
        });
      }
    },
    /**
     *function for user to input 6 digits verify code
     *if server response, proceed to next step, else notify user error
     * @param number verify code
     */
    verifyInput(number) {
      this.verifyCodeMsg = ""
      this.formData.verifyCode += number;
      if (this.formData.verifyCode.length === 6) {
        this.buttonLoading = true;
        UserService.verifyCode(this.formData.email, this.formData.verifyCode)
            .then(resp => {
              if (resp) {
                if (resp.data) {
                  this.verifyCodeInputShow = false;
                  this.currentStep++;
                } else {
                  this.formData.verifyCode = "";
                  this.verifyCodeMsg = "code error, please have a check";
                }
              }
              this.buttonLoading = false;
            })
            .catch(err => {
              console.log(err)
              this.verifyCodeMsg = "too much try, please wait a moment";

              this.buttonLoading = false;
              setTimeout(() => {
                this.verifyCodeInputShow = false;
                this.formData.verifyCode = "";
              }, 500)
              this.buttonLoading = false;
            });
      }
    },

    /**
     *function to delete verify code
     */
    verifyDelete() {
      let verifyCode = this.formData.verifyCode;
      this.formData.verifyCode = verifyCode.substring(0, verifyCode.length - 1);
    },

    /**
     *function to resend verify code
     * if server response, notify user success, else catch error
     */
    resend() {
      UserService.sendVerifyCode(this.formData.email)
          .then(resp => {
            if (resp) {
              ElNotification({
                type: 'success',
                message: "Send verify code successfully"
              });
            }
          })
          .catch(err => {
            console.log(err);
            ElNotification({
              type: 'error',
              message: "Operation too fast"
            });
          })
    },
    init() {
      this.currentStep = 0;
      this.formData.email = "";
      this.formData.password = "";
      this.formData.verifyCode = "";
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