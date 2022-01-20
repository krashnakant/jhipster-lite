import { shallowMount } from '@vue/test-utils';
import HomeVue from '@/common/primary/home/home.vue';

describe('home.vue', () => {
  it('renders props.msg when passed', () => {
    const wrapper = shallowMount(HomeVue, {});
    expect(wrapper.exists()).toBe(true);
  });
});
