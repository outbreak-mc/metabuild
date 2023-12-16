Собирает Minecraft-ресурспак из папки в zip, обходя все ненужные файлы, а также использует специальный конфиг `pack.yml`
для создания `pack.mcmeta`, в котором можно использовать синтаксис MiniMessage в описании ресурспака.

## pack.yml

```yml
# Название, которое будет использовано для архива. 
# Плейсхолдер %dirname% существует по умолчанию и является именем корневой папки
name: "%dirname%-%version%"

placeholders:
  version: 1.0

# Может быть как числом pack_format, так и напрямую версией игры. 
# Программа автоматически подбирает нужный pack_format для версии.
pack-format: "1.20"

# Описание ресурспака с синтаксисом MiniMessage
description: |-
  <#BDB8F6>Resourcepack</#BDB8F6> <#ECAEF6>%version%</#ECAEF6>
  <gray>by</gray> <gradient:#f6895a:#fff16b>ᴏᴜᴛʙʀᴇᴀᴋ</gradient>
```

## Использование

Собрать ресурспак из папки `resourcepack` и сохранить zip в папку `build`:

```shell
java -jar metabuild-1.0.jar zip --input resourcepack --output build
```

---

Создать pack.mcmeta на основе pack.yml в текущей папке:

```shell
java -jar metabuild-1.0.jar mcmeta
```

---

Создать pack.yml и pack.mcmeta в текущей папке:

```shell
java -jar metabuild-1.0.jar init
```

## Команды

### zip

`zip` - пакует папку ресурспака в архив, исключая файлы всех типов, которые не могут содержаться в ресурспаках, а также
любые папки в корне, кроме `assets`.

Параметры:

- `-i, --input <путь>` - путь к папке ресурспака
- `-o, --output <путь>` - путь к папке для сохранения архива

### mcmeta

`mcmeta` - генерирует файл `pack.mcmeta` на основе существующего `pack.yml`.

Параметры:

- `-d, --dir <путь>` - путь к корневой папке ресурспака

### init

Создаёт файл `pack.yml` с конфигурацией по умолчанию и сразу же генерирует `pack.mcmeta`.

Параметры:

- `-d, --dir <путь>` - путь к корневой папке ресурспака
- `--overwrite / --preserve` - перезаписать / отменить выполнение, если файлы существуют (по умолчанию файлы не
  будут
  перезаписаны)