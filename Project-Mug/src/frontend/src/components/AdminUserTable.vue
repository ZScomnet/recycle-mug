<template>
  <div class="table-card">
    <div class="table-header">
      <div class="header-content">
        <div class="content-left">
          <label>Show</label>
          <select class="per-page-selector" v-model="perPage" @change="changePerPage">
            <option value="10">10</option>
            <option v-if="total >= 25" value="25">25</option>
            <option v-if="total >= 50" value="50">50</option>
            <option v-if="total >= 100" value="100">100</option>
          </select>
          <label>entries</label>
        </div>
        <div class="content-right">
          <input type="text" placeholder="Search..." class="content-form" />
          <button :style="{ backgroundColor: userStyle }" @click="onWriting">
            <span>
              <font-awesome-icon :icon="['fas', 'search']"></font-awesome-icon> 유저 검색</span
            >
          </button>
        </div>
      </div>
    </div>
    <div class="table-content">
      <table role="table" aria-busy="false">
        <thead role="rowgroup">
          <tr role="row">
            <th
              role="columnheader"
              scope="col"
              tabindex="0"
              aria-colindex="1"
              aria-sort="descending"
              class="index"
            >
              <div style="text-align:center;">#</div>
            </th>

            <th role="columnheader" scope="col" tabindex="0" aria-colindex="2" class="role">
              <div style="text-align:center;">ID</div>
            </th>

            <th role="columnheader" scope="col" tabindex="0" aria-colindex="3" class="username">
              <div>username</div>
            </th>

            <th role="columnheader" scope="col" tabindex="0" aria-colindex="4" class="last-login">
              <div style="text-align:center;">last Login</div>
            </th>

            <th role="columnheader" scope="col" tabindex="0" aria-colindex="5" class="sign-up-date">
              <div style="text-align:center;">sign up date</div>
            </th>
          </tr>
        </thead>

        <tbody role="rowgroup">
          <tr
            role="row"
            v-for="index in parseInt(perPage)"
            :key="index"
            @click="openModal((currentPage - 1) * perPage + index - 1)"
          >
            <td
              aria-colindex="1"
              role="cell"
              v-if="entryList[(currentPage - 1) * perPage + index - 1]"
              style="text-align:center;"
            >
              {{ index + (currentPage - 1) * perPage }}
            </td>
            <td
              aria-colindex="2"
              role="cell"
              v-if="entryList[(currentPage - 1) * perPage + index - 1]"
              style="text-align:center;"
            >
              {{ entryList[(currentPage - 1) * perPage + index - 1].email }}
            </td>
            <td
              aria-colindex="3"
              role="cell"
              v-if="entryList[(currentPage - 1) * perPage + index - 1]"
              style="display:flex; flex-direction:row; align-items:center"
            >
              <div class="profile-pic-wrapper">
                <img
                  :src="
                    'data:image/jpeg;base64,' +
                      entryList[(currentPage - 1) * perPage + index - 1].profilePicture
                  "
                  alt=""
                  class="profile-pic"
                />
              </div>
              {{ entryList[(currentPage - 1) * perPage + index - 1].nickname }}

              <div class="user-detail">
                <h1>{{ entryList[(currentPage - 1) * perPage + index - 1].nickname }}</h1>
              </div>
            </td>
            <td
              aria-colindex="4"
              role="cell"
              v-if="entryList[(currentPage - 1) * perPage + index - 1]"
              style="text-align:center;"
            >
              {{ entryList[(currentPage - 1) * perPage + index - 1].lastLoginDateTime }}
            </td>
            <td
              aria-colindex="5"
              role="cell"
              v-if="entryList[(currentPage - 1) * perPage + index - 1]"
              style="text-align:center;"
            >
              {{ entryList[(currentPage - 1) * perPage + index - 1].signupDateTIme }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="table-footer">
      <div>
        <div class="footer-left">
          <span
            >Showing {{ (currentPage - 1) * perPage + 1 }} to
            {{ currentPage * perPage > total ? total : currentPage * perPage }} of
            {{ total }} entries</span
          >
        </div>
        <div class="footer-right">
          <ul role="menubar" aria-label="Pagination">
            <li role="presentation">
              <button
                role="menuitem"
                type="button"
                tabindex="-1"
                class="page-item prev-item"
                @click="prevPage"
                v-if="currentPage > 5"
              >
                <font-awesome-icon :icon="['fas', 'angle-left']"></font-awesome-icon>
              </button>
            </li>
            <li role="presentation">
              <button
                role="menuitem"
                type="button"
                tabindex="-1"
                class="page-item first-item"
                :class="{
                  active: currentPage == (Math.ceil(currentPage / 5) - 1) * 5 + 1,
                  'last-item': (Math.ceil(currentPage / 5) - 1) * 5 + 1 == maxPage,
                }"
                :style="[
                  currentPage == (Math.ceil(currentPage / 5) - 1) * 5 + 1
                    ? { backgroundColor: userStyle }
                    : {},
                ]"
                @click="selectPage"
                v-if="(Math.ceil(currentPage / 5) - 1) * 5 + 1 <= maxPage"
              >
                {{ (Math.ceil(currentPage / 5) - 1) * 5 + 1 }}
              </button>
            </li>
            <li role="presentation">
              <button
                role="menuitem"
                type="button"
                tabindex="-1"
                class="page-item"
                :class="{
                  active: currentPage == (Math.ceil(currentPage / 5) - 1) * 5 + 2,
                  'last-item': (Math.ceil(currentPage / 5) - 1) * 5 + 2 == maxPage,
                }"
                :style="[
                  currentPage == (Math.ceil(currentPage / 5) - 1) * 5 + 2
                    ? { backgroundColor: userStyle }
                    : {},
                ]"
                @click="selectPage"
                v-if="(Math.ceil(currentPage / 5) - 1) * 5 + 2 <= maxPage"
              >
                {{ (Math.ceil(currentPage / 5) - 1) * 5 + 2 }}
              </button>
            </li>
            <li role="presentation">
              <button
                role="menuitem"
                type="button"
                tabindex="-1"
                class="page-item"
                :class="{
                  active: currentPage == (Math.ceil(currentPage / 5) - 1) * 5 + 3,
                  'last-item': (Math.ceil(currentPage / 5) - 1) * 5 + 3 == maxPage,
                }"
                :style="[
                  currentPage == (Math.ceil(currentPage / 5) - 1) * 5 + 3
                    ? { backgroundColor: userStyle }
                    : {},
                ]"
                @click="selectPage"
                v-if="(Math.ceil(currentPage / 5) - 1) * 5 + 3 <= maxPage"
              >
                {{ (Math.ceil(currentPage / 5) - 1) * 5 + 3 }}
              </button>
            </li>
            <li role="presentation">
              <button
                role="menuitem"
                type="button"
                tabindex="-1"
                class="page-item"
                :class="{
                  active: currentPage == (Math.ceil(currentPage / 5) - 1) * 5 + 4,
                  'last-item': (Math.ceil(currentPage / 5) - 1) * 5 + 4 == maxPage,
                }"
                :style="[
                  currentPage == (Math.ceil(currentPage / 5) - 1) * 5 + 4
                    ? { backgroundColor: userStyle }
                    : {},
                ]"
                @click="selectPage"
                v-if="(Math.ceil(currentPage / 5) - 1) * 5 + 4 <= maxPage"
              >
                {{ (Math.ceil(currentPage / 5) - 1) * 5 + 4 }}
              </button>
            </li>
            <li role="presentation">
              <button
                role="menuitem"
                type="button"
                tabindex="-1"
                class="page-item last-item"
                :class="{
                  active: currentPage == (Math.ceil(currentPage / 5) - 1) * 5 + 5,
                  'last-item': (Math.ceil(currentPage / 5) - 1) * 5 + 5 == maxPage,
                }"
                :style="[
                  currentPage == (Math.ceil(currentPage / 5) - 1) * 5 + 5
                    ? { backgroundColor: userStyle }
                    : {},
                ]"
                @click="selectPage"
                v-if="(Math.ceil(currentPage / 5) - 1) * 5 + 5 <= maxPage"
              >
                {{ (Math.ceil(currentPage / 5) - 1) * 5 + 5 }}
              </button>
            </li>
            <li role="presentation">
              <button
                role="menuitem"
                type="button"
                tabindex="-1"
                class="page-item next-item"
                @click="nextPage"
                v-if="currentPage <= Math.floor((maxPage - 1) / 5) * 5"
              >
                <font-awesome-icon :icon="['fas', 'angle-right']"></font-awesome-icon>
              </button>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <div v-if="opened" style="position:absolute;top:0;left:0;" @click="closeModal()">
      <div class="modal-wrapper">
        <div class="modal" @click.stop>
          <admin-user-detail
            :user="userDetail"
            :role="role"
            @closeModal="closeModal"
          ></admin-user-detail>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faAngleLeft, faAngleRight, faSearch } from "@fortawesome/free-solid-svg-icons";
import { library as faLibrary } from "@fortawesome/fontawesome-svg-core";
import axios from "axios";
import AdminUserDetail from "./AdminUserDetail.vue";

faLibrary.add(faAngleLeft, faAngleRight, faSearch);

export default {
  data() {
    return {
      total: 10,
      perPage: 10,
      maxPage: 1,
      currentPage: 1,
      entryList: [],
      writingMode: false,
      userStyle: {
        backgroundColor: "#5d9b5e",
      },
      opened: false,
      userDetail: null,
    };
  },
  props: ["role"],
  components: {
    FontAwesomeIcon,
    AdminUserDetail,
  },
  methods: {
    openModal(index) {
      const path = `/backend/${this.role}/${this.entryList[index].id}`;
      let user = axios.create();

      user
        .get(path)
        .then((res) => {
          this.userDetail = res.data;
          this.opened = true;
        })
        .catch((err) => {
          console.log("err :>> ", err);
        });
    },
    closeModal() {
      this.opened = false;
    },
    nextPage() {
      this.currentPage = Math.ceil(this.currentPage / 5) * 5 + 1;
    },
    prevPage() {
      if (this.currentPage > 5) this.currentPage = Math.ceil(this.currentPage / 5) * 5 - 9;
    },
    selectPage() {
      this.currentPage = parseInt(event.target.textContent);
    },
    changePerPage() {
      this.maxPage = Math.ceil(this.total / this.perPage);
    },
    onWriting(e) {
      e.preventDefault();
      this.writingMode = !this.writingMode;
    },
    findAllUsers() {
      const path = `/backend/user/find-all-${this.role}s`;
      let findUsers = axios.create();

      findUsers
        .get(path)
        .then((res) => {
          this.entryList = res.data;
          this.total = this.entryList.length;
          this.maxPage = Math.ceil(this.total / this.perPage);
        })
        .catch((err) => {
          console.log("err :>> ", err);
        });
    },
    setUserStyle() {
      if (this.role === "partner") {
        this.userStyle = "#d49e3e";
      } else if (this.role === "customer") {
        this.userStyle = "#5d9b5e";
      }
    },
  },
  mounted: function() {
    this.findAllUsers();

    this.setUserStyle();
  },
};
</script>

<style lang="scss" scoped>
@each $theme in $themes {
  &.#{map-get($theme, "name")} {
    margin: 0;
    font-size: 1rem;

    .modal-wrapper {
      width: 100%;
      height: 100%;
      background-color: rgba(0, 0, 0, 0.75);
      position: fixed;
      display: flex;
      justify-content: center;
      align-items: center;
      z-index: 99;

      .modal {
        width: 720px;
        height: 800px;
        max-width: 90%;
        max-height: 90%;
        background-color: map-get($map: $theme, $key: "content-background");
        z-index: 999;
        position: fixed;
        border-radius: 6px;
      }
    }

    .table-card {
      box-shadow: $shadow-light;
      background-color: map-get($map: $theme, $key: "content-background");
      border-radius: 0.428rem;
      transition: all 0.3s ease-in-out;
      display: flex;
      flex-direction: column;
      box-sizing: border-box;
      color: map-get($map: $theme, $key: "text-light");
      width: 100%;
      max-width: 1020px;
      margin: 1rem auto;
      overflow: hidden;

      .table-header {
        margin: 1.5rem;
        box-sizing: border-box;

        .header-content {
          margin-right: -1rem;
          margin-left: -1rem;
          display: flex;
          flex-wrap: wrap;
          box-sizing: border-box;

          .content-left {
            margin-bottom: 0;
            display: flex;
            padding-right: 1rem;
            padding-left: 1rem;
            align-items: center;
            justify-content: flex-start;
            width: 50%;
            box-sizing: border-box;

            label {
              color: map-get($map: $theme, $key: "text-light");
              font-size: 0.7rem;
              font-weight: 100;
              letter-spacing: 1px;
            }

            .per-page-selector {
              width: 90px;
              margin-left: 0.5rem;
              margin-right: 0.5rem;
              box-sizing: border-box;
              position: relative;
              display: inline-block;
              color: map-get($map: $theme, $key: "text-light");
              border: 1px solid map-get($map: $theme, $key: "border");
              padding: 0.5rem;
              border-radius: 0.358rem;
              outline: none;
              background-color: transparent;

              option {
                background-color: map-get($map: $theme, $key: "content-background");
                color: map-get($map: $theme, $key: "text-light");
              }
            }
          }
          .content-right {
            margin-bottom: 0;
            display: flex;
            padding-right: 1rem;
            padding-left: 1rem;
            align-items: center;
            justify-content: flex-end;
            width: 50%;
            box-sizing: border-box;

            .content-form {
              margin-right: 1rem;
              padding: 0.438rem 1rem;
              background-color: map-get($map: $theme, $key: "content-background");
              background-clip: padding-box;
              border: 1px solid map-get($map: $theme, $key: "border");
              border-radius: 0.357rem;
              display: inline-block;
              width: 100%;
              line-height: 1.45;
              color: map-get($map: $theme, $key: "text-light");
              font-weight: 400;

              &:focus {
                transition: all 0.3s ease;
                border: 1px solid $main-color;
                border-radius: 0.357rem;
                outline: none;
                box-shadow: $shadow;

                &::placeholder {
                  transition: all 0.3s ease-in-out;
                  padding-left: 5px;
                }
              }
            }

            button {
              cursor: pointer;
              background-color: $main-color;
              box-shadow: none;
              padding: 0.6rem 1.5rem;
              border-radius: 0.358rem;
              outline: none;
              border: none;

              &:hover {
                box-shadow: $shadow-light;
              }

              span {
                white-space: nowrap;
                box-sizing: border-box;
                cursor: pointer;
                text-align: center;
                color: $white;
                font-weight: 500;
                user-select: none;
                line-height: 1;
              }
            }
          }
        }
      }

      .table-content {
        position: relative;
        display: block;
        width: 100%;
        box-sizing: border-box;
        margin-bottom: 1rem;

        table {
          border-bottom-left-radius: 0.357rem;
          border-bottom-right-radius: 0.357rem;
          margin-bottom: 0;
          width: 100%;
          color: map-get($map: $theme, $key: "text-light");
          border-collapse: collapse;
          box-sizing: border-box;
          text-indent: initial;
          border-spacing: 2px;
          border-color: grey;

          thead tr th {
            background-position: right 0.36rem center;
            background-color: map-get($map: $theme, $key: "table");
            padding-right: 1em;
            cursor: pointer;
            border-bottom: 2px solid map-get($map: $theme, $key: "table-border");
            border-top: 1px solid map-get($map: $theme, $key: "table-border");
            outline: none;
            padding: 0.72rem 2rem;
            text-align: left;
            box-sizing: border-box;
            height: 40px;

            &.index {
              width: 10%;
            }
            &.role {
              width: 25%;
            }
            &.username {
              width: 25%;
            }
            &.last-login {
              width: 20%;
            }
            &.sign-up-date {
              width: 20%;
            }

            div {
              cursor: pointer;
              font-size: 0.857rem;
              letter-spacing: 0.5px;
              text-transform: uppercase;
            }
          }

          tbody tr td {
            padding: 0.72rem 2rem;
            vertical-align: middle;
            box-sizing: border-box;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            font-weight: 100;
            cursor: pointer;

            .user-detail {
              display: none;
            }

            .profile-pic-wrapper {
              width: 1.5rem;
              height: 1.5rem;
              border-radius: 50%;
              overflow: hidden;
              margin-right: 1rem;

              img {
                width: 100%;
                height: 100%;
                object-fit: cover;
              }
            }

            &:hover {
              text-decoration: underline;
              font-weight: 400;
            }
          }
          tbody tr:hover {
            background-color: transparentize(map-get($map: $theme, $key: "table"), 0.2);
          }
          tr {
            border-bottom: 1px solid map-get($map: $theme, $key: "table-border");

            td.active {
              background-color: $main-color;
            }
          }
        }
      }

      .table-footer {
        margin: 0 1.5rem 1.5rem 1.5rem;
        box-sizing: border-box;

        div {
          margin: 0;
          display: flex;
          flex-wrap: wrap;
          box-sizing: border-box;

          div {
            padding: 0 1rem;
            font-size: 0.95rem;
            position: relative;
            box-sizing: border-box;
          }

          .footer-left {
            color: map-get($map: $theme, $key: "text-muted");
            @media (min-width: 576px) {
              justify-content: flex-start;
              flex: 0 0 50%;
              max-width: 50%;
            }
            justify-content: center;
            align-items: center;
            flex: 0 0 100%;
            max-width: 100%;
            font-weight: 100;
            letter-spacing: 1px;
          }

          .footer-right {
            @media (min-width: 576px) {
              justify-content: flex-end;
              flex: 0 0 50%;
              max-width: 50%;
            }
            justify-content: center;
            align-items: center;
            flex: 0 0 100%;
            max-width: 100%;

            ul {
              border-radius: 0.357rem;
              display: flex;
              list-style: none;
              box-sizing: border-box;
              align-items: center;

              .prev-item {
                margin-right: 0.35rem;
                border-radius: 50%;
              }
              .next-item {
                margin-left: 0.35rem;
                border-radius: 50%;
              }
              .first-item {
                border-bottom-left-radius: 50%;
                border-top-left-radius: 50%;
              }
              .last-item {
                border-bottom-right-radius: 50%;
                border-top-right-radius: 50%;
              }

              .page-item {
                display: flex;
                justify-content: center;
                align-items: center;
                padding: 0.5rem 0;
                border: none;
                transition: all 0.2s ease-out;
                line-height: 1.3;
                cursor: pointer;
                font-size: 1rem;
                min-width: 2.286rem;
                display: flex;
                align-items: center;
                justify-content: center;
                background-color: map-get($map: $theme, $key: "table");
                position: relative;
                color: map-get($map: $theme, $key: "text-light");
                outline: none;

                &:hover {
                  background-color: map-get($map: $theme, $key: "text-muted");
                  color: $white;
                  transition: all 0.3s ease-in-out;
                }

                &.active {
                  background-color: $main-color;
                  color: $white;
                  transition: all 0.3s ease-in-out;
                }
              }
            }
          }
        }
      }
    }
  }
}
</style>
