
# Prueba tecnica
Construir API para manejar lista de franquicias. Una franquisia se compone por un nombre y un listado de surcursales y, a su vez, una surcursar esta compuesta por un nombre  y un listado de productos ofertados en la surcursal. Un producto tiene nombre y contidad en stock.

## Tecnologias Requeridas
1. Springboot

## Objetivos
2. exponer api para agregar una nueva franquicia
3. exponer api para agregar una nueva surcursal a una franquicia
4. exponer api para agregar un nuevo producto a una surcursal
5. exponer api para eliminar un producto de una surcursal
6. exponer end point para modificar stock del producto
7. exponer end point para mostrar cual es el prodcuto que mas stock tiene por surcursal por franquicia, debe retornar un listado que indique a que surcursal pertenece
8. Usar Dynamo db de AWS

## Extra
- plus si se empaqueta con docker
- plus si se usa programacion funcional, ractiva
- plus si se xpone end point que permita actualizr el npmbre de una franquicia
- plus si se xpone end point que permita actualizr el npmbre de una surcursal
- plus si se aprovisona la persistencia de datos como infraestructura como codigo como Cloudformation
- Plpus si todo se despliega en la nube

## Notas
se tendra en cuenta el flujo de trabao usando git, la prueba debe er presentador en un repo con acceso publico.
se debe incluir docuemtnacion