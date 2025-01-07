using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using usuarios.Models;
using usuarios.Repositories;

namespace usuarios.Services
{
    public class ServicioUsuarios
    {
        private readonly RepositorioUsuarios _repositorioUsuarios;
        private readonly RepositorioCodigosActivacion _repositorioCodigosActivacion;

        public ServicioUsuarios()
        {
            _repositorioUsuarios = new RepositorioUsuarios();
            _repositorioCodigosActivacion = new RepositorioCodigosActivacion();
        }

        // Solicitud código de activación (Gestor)
        public async Task<string> SolicitarCodigoActivacion(string idUsuario)
        {
            var codigo = new CodigoActivacionModelo(idUsuario, TimeSpan.FromDays(1));
            await _repositorioCodigosActivacion.InsertarAsync(codigo);
            return codigo.Codigo;
        }

        // Alta de usuario con usuario/contraseña
        public async Task<bool> AltaUsuario(
            string idUsuario,
            string codigoActivacion,
            string nombreCompleto,
            string correoElectronico,
            string? telefono,
            string? direccionPostal,
            string nombreUsuario,
            string contrasena,
            Rol rol)
        {
            // Validar código de activación
            var codigos = await _repositorioCodigosActivacion.ObtenerTodosAsync();
            var codigoValido = codigos.FirstOrDefault(c => c.Codigo == codigoActivacion && c.UsuarioId == idUsuario && c.EsValido());

            if (codigoValido == null)
            {
                return false; // Código inválido, expirado o no coincide con el ID proporcionado
            }

            // Verificar si el usuario ya existe
            var usuarioExistente = await _repositorioUsuarios.ObtenerPorIdAsync(idUsuario);
            if (usuarioExistente != null)
            {
                return false; // Usuario con el mismo ID ya existe
            }

            // Construir el modelo de usuario
            var nuevoUsuario = new Usuario
            {
                Id = idUsuario, // Asignar el ID proporcionado por el gestor
                NombreCompleto = nombreCompleto,
                CorreoElectronico = correoElectronico,
                Telefono = telefono,
                DireccionPostal = direccionPostal,
                Nombre = nombreUsuario,
                Contrasena = contrasena, // Encriptar la contraseña antes de guardarla
                Rol = rol,
                FechaRegistro = DateTime.UtcNow
            };

            // Crear el usuario en la base de datos
            await _repositorioUsuarios.InsertarAsync(nuevoUsuario);

            // Eliminar el código de activación para evitar reutilización
            await _repositorioCodigosActivacion.EliminarAsync(codigoValido.Id);

            return true;
        }

        // Alta de usuario con OAuth2
        public async Task<bool> AltaUsuario(
            string idUsuario,
            string codigoActivacion,
            string nombreCompleto,
            string correoElectronico,
            string? telefono,
            string? direccionPostal,
            string identificadorOAuth2,
            Rol rol)
        {
            // Validar código de activación
            var codigos = await _repositorioCodigosActivacion.ObtenerTodosAsync();
            var codigoValido = codigos.FirstOrDefault(c => c.Codigo == codigoActivacion && c.UsuarioId == idUsuario && c.EsValido());

            if (codigoValido == null)
            {
                return false; // Código inválido, expirado o no coincide con el ID proporcionado
            }

            // Verificar si el usuario ya existe
            var usuarioExistente = await _repositorioUsuarios.ObtenerPorIdAsync(idUsuario);
            if (usuarioExistente != null)
            {
                return false; // Usuario con el mismo ID ya existe
            }

            // Construir el modelo de usuario
            var nuevoUsuario = new Usuario
            {
                Id = idUsuario, // Asignar el ID proporcionado por el gestor
                NombreCompleto = nombreCompleto,
                CorreoElectronico = correoElectronico,
                Telefono = telefono,
                DireccionPostal = direccionPostal,
                IdentificadorOAuth2 = identificadorOAuth2,
                Rol = rol,
                FechaRegistro = DateTime.UtcNow
            };

            // Crear el usuario en la base de datos
            await _repositorioUsuarios.InsertarAsync(nuevoUsuario);

            // Eliminar el código de activación para evitar reutilización
            await _repositorioCodigosActivacion.EliminarAsync(codigoValido.Id);

            return true;
        }


        // Baja de usuario (Gestor)
        public async Task<bool> BajaUsuario(string idUsuario)
        {
            var usuario = await _repositorioUsuarios.ObtenerPorIdAsync(idUsuario);
            if (usuario == null)
            {
                return false; // Usuario no encontrado
            }

            await _repositorioUsuarios.EliminarAsync(idUsuario);
            return true;
        }

        // Verificar credenciales (Usuario)
        public async Task<List<Claim>> VerificarCredenciales(string nombreUsuario, string contrasena)
        {
            var usuarios = await _repositorioUsuarios.ObtenerTodosAsync();
            var usuario = usuarios.FirstOrDefault(u => u.NombreUsuario == nombreUsuario && u.Contrasena == contrasena);

            if (usuario == null)
            {
                return null; // Credenciales inválidas
            }

            // Retornar claims con los valores solicitados
            return new List<Claim>
    {
        new Claim(ClaimTypes.NameIdentifier, usuario.Id), // Identificador del usuario
        new Claim(ClaimTypes.Name, usuario.NombreCompleto), // Nombre completo
        new Claim(ClaimTypes.Role, usuario.Rol.ToString()) // Rol
    };
        }

        // Verificar usuario OAuth2 (Usuario)
        public async Task<List<Claim>> VerificarUsuarioOAuth2(string identificadorOAuth2)
        {
            var usuarios = await _repositorioUsuarios.ObtenerTodosAsync();
            var usuario = usuarios.FirstOrDefault(u => u.IdentificadorOAuth2 == identificadorOAuth2);

            if (usuario == null)
            {
                return null; // Usuario no encontrado
            }

            // Retornar claims con los valores solicitados
            return new List<Claim>
    {
        new Claim(ClaimTypes.NameIdentifier, usuario.Id), // Identificador del usuario
        new Claim(ClaimTypes.Name, usuario.NombreCompleto), // Nombre completo
        new Claim(ClaimTypes.Role, usuario.Rol.ToString()) // Rol
    };
        }



        // Listar usuarios (Gestor)
        public async Task<IEnumerable<Usuario>> ListarUsuarios()
        {
            return await _repositorioUsuarios.ObtenerTodosAsync();
        }
    }
}
