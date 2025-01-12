using System;
using System.Threading.Tasks;
using usuarios.Models;
using usuarios.Repositories;

namespace usuarios.Test
{
    public class TestRepositorio
    {
        public static async Task Main(string[] args)
        {
            Console.WriteLine("Iniciando pruebas de los repositorios...");

            // Crear instancias de los repositorios
            var repositorioUsuarios = new RepositorioUsuarios();
            var repositorioCodigosActivacion = new RepositorioCodigosActivacion();

            // Pruebas con usuarios
            Console.WriteLine("Creando un nuevo usuario...");
            var nuevoUsuario = new Usuario
            {
                NombreCompleto = "Juan Pérez",
                
                CorreoElectronico = "juan.perez@example.com",
                Telefono = "123456789",
                DireccionPostal = "Calle Falsa 123",
                Nombre = "juanp",
                Contrasena = "password123",
                Rol = Rol.USER
            };

            await repositorioUsuarios.InsertarAsync(nuevoUsuario);
            Console.WriteLine($"Usuario creado con ID: {nuevoUsuario.Id}");

            Console.WriteLine("Listando usuarios...");
            var usuarios = await repositorioUsuarios.ObtenerTodosAsync();
            foreach (var usuario in usuarios)
            {
                Console.WriteLine($"ID: {usuario.Id}, Nombre: {usuario.NombreCompleto}, Correo: {usuario.CorreoElectronico}");
            }

            // Pruebas con códigos de activación
            Console.WriteLine("Creando un nuevo código de activación...");
            var nuevoCodigo = new CodigoActivacion(nuevoUsuario.Id, TimeSpan.FromDays(1));

            await repositorioCodigosActivacion.InsertarAsync(nuevoCodigo);
            Console.WriteLine($"Código de activación creado con ID: {nuevoCodigo.Id}, Código: {nuevoCodigo.Codigo}");

            Console.WriteLine("Listando códigos de activación...");
            var codigos = await repositorioCodigosActivacion.ObtenerTodosAsync();
            foreach (var codigo in codigos)
            {
                Console.WriteLine($"ID: {codigo.Id}, UsuarioID: {codigo.UsuarioId}, Código: {codigo.Codigo}, Expiración: {codigo.FechaExpiracion}");
            }

            /*
            // Prueba de eliminación
            Console.WriteLine("Eliminando el usuario y su código de activación...");
            await repositorioUsuarios.EliminarAsync(nuevoUsuario.Id);
            await repositorioCodigosActivacion.EliminarAsync(nuevoCodigo.Id);

            Console.WriteLine("Prueba finalizada. Usuario y código eliminados.");
            */
        }
    }
}
