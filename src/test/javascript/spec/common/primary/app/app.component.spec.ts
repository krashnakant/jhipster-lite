import { shallowMount } from '@vue/test-utils';
import AppVue from '@/common/primary/app/app.vue';

describe('app.vue', () => {
  it('renders props.msg when passed', () => {
    const wrapper = shallowMount(AppVue, {});
    expect(wrapper.exists()).toBe(true);
  });
});
