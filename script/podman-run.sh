#!/bin/bash

# Copyright (C) 2023 Claudio Nave
#
# This file is part of UserDemo.
#
# UserDemo is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# UserDemo is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.

podman pull docker.io/evaristegalois/user-demo
podman pull docker.io/library/postgres

podman pod create --name user-demo-pod -p 8080:8080 -p 5432:5432

podman run -d --pod user-demo-pod -e POSTGRES_PASSWORD=password -e POSTGRES_USER=user --name postgres docker.io/library/postgres
podman run -d --pod user-demo-pod --requires postgres --name user-demo docker.io/evaristegalois/user-demo