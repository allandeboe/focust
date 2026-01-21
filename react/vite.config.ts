/**
 * vite.config.ts - Configuration for Vite
 * Copyright (C) 2025  Allan DeBoe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * ----------------------------------------------------------------------
 * 
 * @author  Allan DeBoe <allan.m.deboe@gmail.com>
 * @date    May 17th, 2025
 */

import { defineConfig } from 'vite';

import react from '@vitejs/plugin-react';
import tailwindcss from '@tailwindcss/vite';

// "max-age" is 1 year long
const MAX_AGE = 365 * 24 * 60 * 60

// https://vite.dev/config/
export default defineConfig({
  server: {
    port: 5443,
    host: true,
    https: true,
    headers: {
      'Strict-Transport-Security': `max-age=${MAX_AGE}`
    }
  },
  plugins: [
    react(),
    tailwindcss(),
  ],
});
