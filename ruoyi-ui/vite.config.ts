import { defineConfig, loadEnv } from 'vite';
import createVitePlugins from './vite/plugins';
import path, {join} from 'path';

// 创建版本号(年月日时分)
function getDirName() {
  var d = new Date();
  var yy = d.getFullYear().toString().slice(2);
  var MM = d.getMonth() + 1 >= 10 ? (d.getMonth() + 1) : '0' + (d.getMonth() + 1);
  var DD = d.getDate() >= 10 ? d.getDate() : '0' + d.getDate();
  var h  = d.getHours() >= 10 ? d.getHours() : '0' + d.getHours();
  var mm = d.getMinutes() >= 10 ? d.getMinutes() : '0' + d.getMinutes();
  return '' + yy + MM + DD + h + mm;
};

export default defineConfig(({ mode, command }) => {
  const env = loadEnv(mode, process.cwd());
  const { VITE_APP_ENV } = env;
  let version = getDirName()
  return {
    plugins: createVitePlugins(env, command === 'build'),
    // 部署生产环境和开发环境下的URL。
    // 默认情况下，vite 会假设你的应用是被部署在一个域名的根路径上
    // 例如 https://www.ruoyi.vip/。如果应用被部署在一个子路径上，你就需要用这个选项指定这个子路径。例如，如果你的应用被部署在 https://www.ruoyi.vip/admin/，则设置 baseUrl 为 /admin/。
    base: VITE_APP_ENV === 'production' ? '/' : '/',
    server: {
        port: 8082,
        host: true,
        open: true,
        proxy: {
            // https://cn.vitejs.dev/config/#server-proxy
            '/dev-api': {
              target: 'http://localhost:8081',
              changeOrigin: true,
              rewrite: p => p.replace(/^\/dev-api/, ''),
            },
            // '/dev-api': {
            //   target: 'http://localhost:8081',
            //   changeOrigin: true,
            //   rewrite: p => p.replace(/^\/dev-api/, ''),
            // },
        },
    },
    resolve: {
        alias: {
            // 设置路径
            '~': path.resolve(__dirname, './'),
            // 设置别名
            '@': path.resolve(__dirname, './src'),
        },
    },
    build: {
      chunkSizeWarningLimit: 1500,
      assetsDir: version,
      terserOptions: {
        // 清除debugger及console.log的打印信息
        compress: {
          drop_console: true,
          drop_debugger: true
        }
      },
      rollupOptions: {
        output: {
          manualChunks(id) {
            if (id.includes('node_modules')) { // 超大静态资源拆分
              return id.toString().split('node_modules/')[1].split('/')[0].toString()
            }
          },
          chunkFileNames: `${version}/static/js/[name]-[hash].js`,
          entryFileNames: `${version}/static/js/[name]-[hash].js`,
          assetFileNames: (file) => {
            const fileName = file.name
            if (/\.(png|jpe?g|gif|svg|webp)$/i.test(fileName)) {
              return `${version}/static/images/[name]-[hash].[ext]`
            } else {
             return `${version}/static/[ext]/[name]-[hash].[ext]`
            }
          }
        },
      },
    },
  };
});
