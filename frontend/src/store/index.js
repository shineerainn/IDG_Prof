import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    user: {
      id: null,
      role: null,
    },
  },
  getters: {
    userId: (state) => state.user.id,
    userRole: (state) => state.user.role,
  },
  mutations: {
    setUserId(state, id) {
      state.user.id = id;
    },
    setUserRole(state, role) {
      state.user.role = role;
    },
  },
  actions: {},
  modules: {},
});
