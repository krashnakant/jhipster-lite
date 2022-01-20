const { pathsToModuleNameMapper } = require('ts-jest/utils');
const {
  compilerOptions: { paths = {}, baseUrl = './' },
} = require('./tsconfig.json');

module.exports = {
  rootDir: './',
  preset: '@vue/cli-plugin-unit-jest/presets/typescript',
  roots: ['<rootDir>', `<rootDir>/${baseUrl}`, `<rootDir>/src/test/javascript/spec/app/`],
  transform: {
    '^.+\\.vue$': 'vue-jest',
  },
  moduleNameMapper: {
    '^@/(.*)$': '<rootDir>/src/$1',
    ...pathsToModuleNameMapper(paths, { prefix: `<rootDir>/${baseUrl}/` }),
  },
  testMatch: ['<rootDir>/src/test/javascript/spec/**/@(*.)@(spec.ts)'],
  collectCoverage: true,
  collectCoverageFrom: [
    'src/main/webapp/**/*.{js,ts,vue}',
    '!src/main/webapp/**/*.component.ts',
    '!src/main/webapp/app/main.ts',
    '!src/main/webapp/app/router/*.ts',
    '!src/i18n.ts',
    '!**/*.d.ts',
    '!**/*AsyncModule.ts',
  ],
  coveragePathIgnorePatterns: [
    '<rootDir>/node_modules/',
    '<rootDir>/src/test/javascript/',
    '<rootDir>/src/main/webapp/app/router',
    '<rootDir>/src/main/resources',
    '.*.json',
  ],
  coverageReporters: ['html', 'json-summary', 'text-summary', 'lcov', 'clover'],
  coverageDirectory: '<rootDir>/target/test-results/',
  testResultsProcessor: 'jest-sonar-reporter',
};
