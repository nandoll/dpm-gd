<?php

namespace App\Database\Seeds;

use App\Models\Rol;
use CodeIgniter\Database\Seeder;
use Faker\Factory;

class PersonaSeeder extends Seeder
{

    public function run()
    {
        $this->db->query("TRUNCATE TABLE personas");
        helper('secure_password');
        for ($i = 0; $i < 10; $i++) { //to add 10 clients. Change limit as desired
            $this->db->table('personas')->insert($this->generatePersonas());
        }
    }

    private function generatePersonas(): array
    {
        $faker = Factory::create();
        $rol = new Rol();
        $dataRol = $rol->asObject()->orderBy('RAND()')->first();
        return [
            'correo' => $faker->email,
            'password' => hashPassword('123123'),
            'nombres' => $faker->name(),
            'estado' => random_int(0,1),
            'especialidad' => $faker->randomElement( $this->professions()),
            'idJefe' => 0,
            'rol_id' => $dataRol->id
        ];
    }

    private function professions(): array{
        $professions = array(
            "Técnico en Topografía",
            "Técnico en comunicación audiovisual",
            "Técnico en seguridad ocupacional",
            "Técnico en telecomunicaciones",
            "Técnico en construcción",
            "Técnico en electrónica industrial",
            "Técnico en arquitectura y urbanismo"
        );
        return $professions;
    }
}